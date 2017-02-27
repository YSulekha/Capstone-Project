package com.haryadi.capstone_stage2.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.haryadi.capstone_stage2.R;
import com.haryadi.capstone_stage2.data.TriggerContract;
import com.haryadi.capstone_stage2.utils.ChangeSettings;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateProfileFragment extends DialogFragment {

    @BindView(R.id.media_seekbar)
    SeekBar mediaVolumeBar;
    @BindView(R.id.ring_seekbar)
    SeekBar ringVolumeBar;
    @BindView(R.id.content_isbluetoothon)
    Spinner mIsBluetoothOn;
    @BindView(R.id.content_iswifion)
    Spinner mIsWifiOn;
    @BindView(R.id.button_save)
    Button saveButton;
    @BindView(R.id.spinner_wifi_name)
    Spinner mWifiName;
    @BindView(R.id.isConnect)
    RadioGroup radioGroup;
    @BindView(R.id.dialog_radio_connect)
    RadioButton radioConnect;
    @BindView(R.id.dialog_radio_disconnect)
    RadioButton radioDisConnect;
    @BindView(R.id.name)ImageButton imageButton;
    @BindView(R.id.location_wifi_name)EditText locationName;
    String triggerPoint;
    ArrayList<String> names;
    ArrayAdapter<CharSequence> optionsAdapter;
    ArrayAdapter<String> arrayAdapter;
    String isConnect;
    boolean isEdit = false;
    Uri mUri;



    public CreateProfileFragment() {

    }

    public static void newInstance(Fragment f,String triggerPoint, boolean isEdit, Uri uri) {
      //  CreateProfileFragment frag = new CreateProfileFragment();
        Bundle args = new Bundle();
        Log.v("titleAct", triggerPoint);
        args.putString("title", triggerPoint);
        args.putBoolean("isEdit", isEdit);
        Log.v("isEdit", String.valueOf(isEdit));
        if (uri != null) {
            args.putString("Uri", uri.toString());
        }
        f.setArguments(args);
       // return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_create, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        isConnect = getString(R.string.text_connect);
        triggerPoint = getArguments().getString("title", "Enter Name");
        isEdit = getArguments().getBoolean("isEdit");
    /*    if (triggerPoint.equals(getString(R.string.bluetooth))) {
            names = ChangeSettings.getConfiguredBluetooth(getActivity());
            imageButton.setImageResource(R.drawable.ic_bluetooth_black_24dp);
        }*/
        configureViews();
        if (isEdit) {
            mUri = Uri.parse(getArguments().getString("Uri"));
            configureValues(mUri);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                onRadioButtonClicked(checkedId);
            }
        });
    }

    public void onRadioButtonClicked(int id) {
        switch (id) {
            case R.id.dialog_radio_connect:
                if (radioConnect.isChecked()) {
                    isConnect = getString(R.string.text_connect);
                }
                break;
            case R.id.dialog_radio_disconnect:
                if (radioDisConnect.isChecked()) {
                    isConnect = getString(R.string.text_disconnect);
                }
                break;
            default:
                isConnect = getString(R.string.text_connect);
        }
    }

    @OnClick(R.id.button_save)
    public void onClickSave(View view) {
        if (mWifiName.getSelectedItem()==null) {
            Toast.makeText(getActivity(),"Please turn on the "+triggerPoint+" to enter the name",Toast.LENGTH_LONG).show();
            this.dismiss();
        }
        else {
            if (!checkForDuplicates()) {
                insertRecord();
                this.dismiss();

            }
        }
    }

    private void configureViews() {
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, names);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mWifiName.setAdapter(arrayAdapter);
        setVolumeSeekBar();
        optionsAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.options, android.R.layout.simple_spinner_item);
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIsBluetoothOn.setAdapter(optionsAdapter);
        mIsWifiOn.setAdapter(optionsAdapter);
        radioConnect.setChecked(true);
    }

    private void configureValues(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        int pos = 0;
        if (cursor.moveToNext()) {
            String triggerPoint = cursor.getString(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_TRIGGER_POINT));
            if (triggerPoint.equals(getString(R.string.wifi))) {
                mIsWifiOn.setSelection(0);
                mIsWifiOn.setEnabled(false);
                names = ChangeSettings.getConfiguredWifi(getActivity());
                arrayAdapter.notifyDataSetChanged();
                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mWifiName.setAdapter(arrayAdapter);
                pos = arrayAdapter.getPosition(cursor.getString(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_NAME)));
            } else if (triggerPoint.equals(getString(R.string.bluetooth))) {
                mIsBluetoothOn.setSelection(0);
                mIsBluetoothOn.setEnabled(false);
                names = ChangeSettings.getConfiguredBluetooth(getActivity());
                arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, names);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mWifiName.setAdapter(arrayAdapter);
                pos = arrayAdapter.getPosition(cursor.getString(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_NAME)));
            }
            if (cursor.getString(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_CONNECT)).equals(getString(R.string.text_connect))) {
                radioConnect.setChecked(true);
                radioGroup.setEnabled(false);

            } else {
                radioDisConnect.setChecked(true);
                radioGroup.setEnabled(false);
            }
            radioConnect.setEnabled(false);
            radioDisConnect.setEnabled(false);
            mWifiName.setSelection(pos);
            mWifiName.setEnabled(false);
            mIsBluetoothOn.setSelection(optionsAdapter.getPosition(cursor.getString(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_ISBLUETOOTHON))));
            mIsWifiOn.setSelection(optionsAdapter.getPosition(cursor.getString(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_ISWIFION))));
            mediaVolumeBar.setProgress(cursor.getInt(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_MEDIAVOL)));
            ringVolumeBar.setProgress(cursor.getInt(cursor.getColumnIndex(TriggerContract.TriggerEntry.COLUMN_RINGVOL)));
            cursor.close();
        }
    }

    private void insertRecord() {
        ContentValues values = new ContentValues();
        Uri uri = TriggerContract.TriggerEntry.CONTENT_URI;
        String name = mWifiName.getSelectedItem().toString();
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME, name);
        values.put(TriggerContract.TriggerEntry.COLUMN_MEDIAVOL, mediaVolumeBar.getProgress());
        values.put(TriggerContract.TriggerEntry.COLUMN_RINGVOL, ringVolumeBar.getProgress());
        values.put(TriggerContract.TriggerEntry.COLUMN_ISBLUETOOTHON, mIsBluetoothOn.getSelectedItem().toString());
        values.put(TriggerContract.TriggerEntry.COLUMN_ISWIFION, mIsWifiOn.getSelectedItem().toString());
        if (isEdit) {
            updateRecord(values);
        } else {
            ChangeSettings.addWifiNameToSharedPreference(getActivity());
            values.put(TriggerContract.TriggerEntry.COLUMN_TRIGGER_POINT, triggerPoint);
            values.put(TriggerContract.TriggerEntry.COLUMN_TRIGGER_NAME, triggerPoint + "_" + name);
            Log.v("CreateProfile",name+isConnect);
            values.put(TriggerContract.TriggerEntry.COLUMN_CONNECT, isConnect);
            getActivity().getContentResolver().insert(uri, values);
        }
    }


    private boolean checkForDuplicates() {
        if (!isEdit) {

            String name = mWifiName.getSelectedItem().toString();
            Uri uri = TriggerContract.TriggerEntry.CONTENT_URI;
            String where = TriggerContract.TriggerEntry.TABLE_NAME + "." +
                    TriggerContract.TriggerEntry.COLUMN_TRIGGER_NAME + " = ?" + " AND " +
                    TriggerContract.TriggerEntry.COLUMN_CONNECT + " = ?";
            String[] args = new String[]{triggerPoint + "_" + name, isConnect};
            Cursor c = getActivity().getContentResolver().query(uri, null, where, args, null);
            if (c.moveToNext()) {
                Toast.makeText(getActivity(), getString(R.string.text_duplicates), Toast.LENGTH_LONG).show();
                c.close();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private void updateRecord(ContentValues values) {
        String where = TriggerContract.TriggerEntry.TABLE_NAME + "." +
                TriggerContract.TriggerEntry._ID + " = ?";
        String[] args = new String[]{Long.toString(TriggerContract.TriggerEntry.getIdFromUri(mUri))};
        getActivity().getContentResolver().update(TriggerContract.TriggerEntry.CONTENT_URI, values, where, args);
    }

    private void setVolumeSeekBar() {
        final AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        mediaVolumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        ringVolumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        mediaVolumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        ringVolumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_RING));
    }
}
