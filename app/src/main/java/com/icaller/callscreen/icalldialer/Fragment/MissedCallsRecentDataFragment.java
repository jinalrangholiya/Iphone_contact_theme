package com.icaller.callscreen.icalldialer.Fragment;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Adapter.CallLogAdapter;
import com.icaller.callscreen.icalldialer.CustomLoadProgressDialogView;
import com.icaller.callscreen.icalldialer.Model.CallLogDataItem;
import com.icaller.callscreen.icalldialer.SwipeToDeleteDataCallbackContact;
import com.icaller.callscreen.icalldialer.Utils.FavoriteContactRemoverItemTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class MissedCallsRecentDataFragment extends Fragment {
    private static  int REQUEST_READ_CALL_LOG = 1;
    private CallLogAdapter callLogAdapter;
    CustomLoadProgressDialogView customProgressDialog;
    int missedCallCount = 0;
    Map<String, Integer> missedCallCounts = new HashMap();
    LinearLayout nodata;
    ImageView nodataimage;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_missed_calls_recent_file, viewGroup, false);
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        this.nodata = (LinearLayout) inflate.findViewById(R.id.nodata);
        this.nodataimage = (ImageView) inflate.findViewById(R.id.nodataimage);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.customProgressDialog = new CustomLoadProgressDialogView(getActivity());
        if (checkPermission()) {
            fetchCallLogs();
        } else {
            requestPermission();
        }
        if (getActivity().getSharedPreferences(getActivity().getPackageName(), 0).getBoolean("dark_theme", false)) {
            this.nodataimage.setImageResource(R.drawable.favourite);
            getActivity().setTheme(R.style.AppThemeContactContact1);
        } else {
            this.nodataimage.setImageResource(R.drawable.favourite);
            getActivity().setTheme(R.style.AppThemeContactContact);
        }
        new SwipeToDeleteDataCallbackContact(getActivity(), this.recyclerView) {
            @Override
            public void instantiateUnderlayButton( RecyclerView.ViewHolder viewHolder, List<SwipeToDeleteDataCallbackContact.UnderlayButton> list) {
                list.add(new SwipeToDeleteDataCallbackContact.UnderlayButton(MissedCallsRecentDataFragment.this.getActivity(), "", R.drawable.delete_btn, Color.parseColor("#FF3C30"), new SwipeToDeleteDataCallbackContact.UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int i) {
                        String number = MissedCallsRecentDataFragment.this.callLogAdapter.callLogs.get(viewHolder.getAdapterPosition()).getNumber();
                        Log.e("TAG", "onClick:***************** " + number);
                        MissedCallsRecentDataFragment.this.callLogAdapter.removeItem(viewHolder.getAdapterPosition());
                        FavoriteContactRemoverItemTask.removeRecentCallLogs(MissedCallsRecentDataFragment.this.getActivity().getContentResolver(), number);
                        if (MissedCallsRecentDataFragment.this.callLogAdapter.callLogs.size() == 0) {
                            MissedCallsRecentDataFragment.this.nodata.setVisibility(0);
                            MissedCallsRecentDataFragment.this.recyclerView.setVisibility(8);
                        } else {
                            MissedCallsRecentDataFragment.this.nodata.setVisibility(8);
                            MissedCallsRecentDataFragment.this.recyclerView.setVisibility(0);
                        }
                        Toast.makeText(MissedCallsRecentDataFragment.this.getActivity(), "Delete Contact", 0).show();
                    }
                }));
            }
        }.attachSwipe();
        return inflate;
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_CALL_LOG") == 0;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_CALL_LOG"}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 1) {
            if (iArr.length > 0 && iArr[0] == 0) {
                fetchCallLogs();
            } else {
                Toast.makeText(getActivity(), "Permission denied", 0).show();
            }
        }
    }

    private void fetchCallLogs() {
        Cursor query;
        this.customProgressDialog.show();
        ArrayList arrayList = new ArrayList();
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_CALL_LOG") == 0 && (query = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, "date DESC")) != null) {
            int columnIndex = query.getColumnIndex("number");
            int columnIndex2 = query.getColumnIndex("date");
            int columnIndex3 = query.getColumnIndex("type");
            int columnIndex4 = query.getColumnIndex("name");
            HashMap hashMap = new HashMap();
            while (query.moveToNext()) {
                String string = query.getString(columnIndex);
                String string2 = query.getString(columnIndex4);
                long j = query.getLong(columnIndex2);
                int i = query.getInt(columnIndex3);
                if (i == 3) {
                    this.missedCallCount++;
                }
                if (string != null && !string.trim().isEmpty()) {
                    if (hashMap.containsKey(string)) {
                        hashMap.put(string, Integer.valueOf(((Integer) hashMap.get(string)).intValue() + 1));
                    } else {
                        hashMap.put(string, 1);
                    }
                    if (i == 3) {
                        Map<String, Integer> map = this.missedCallCounts;
                        map.put(string, Integer.valueOf(map.getOrDefault(string, 0).intValue() + 1));
                    }
                    if (string != null && i == 3) {
                        arrayList.add(new CallLogDataItem(string2 == null ? "" : string2, string, new Date(j), i, this.missedCallCounts.size()));
                    }
                }
            }
            query.close();
        }
        List<CallLogDataItem> uniqueCallLogs = getUniqueCallLogs(arrayList);
        if (uniqueCallLogs.size() == 0) {
            this.recyclerView.setVisibility(8);
            this.nodata.setVisibility(0);
        } else {
            this.recyclerView.setVisibility(0);
            this.nodata.setVisibility(8);
        }
        CallLogAdapter callLogAdapter = new CallLogAdapter(uniqueCallLogs, getActivity());
        this.callLogAdapter = callLogAdapter;
        this.recyclerView.setAdapter(callLogAdapter);
        this.customProgressDialog.dismiss();
    }

    private List<CallLogDataItem> getUniqueCallLogs(List<CallLogDataItem> list) {
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        for (CallLogDataItem callLogItem : list) {
            if (!hashSet.contains(callLogItem.getNumber())) {
                arrayList.add(callLogItem);
                hashSet.add(callLogItem.getNumber());
            }
        }
        return arrayList;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCallLogs();
    }
}
