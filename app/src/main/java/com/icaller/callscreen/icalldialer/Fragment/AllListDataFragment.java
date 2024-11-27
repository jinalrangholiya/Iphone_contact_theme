package com.icaller.callscreen.icalldialer.Fragment;

import android.app.Activity;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Adapter.CallLogAllAdapter;
import com.icaller.callscreen.icalldialer.CustomLoadProgressDialogView;
import com.icaller.callscreen.icalldialer.Model.CallLogDataItem;
import com.icaller.callscreen.icalldialer.SwipeToDeleteDataCallbackContact;
import com.icaller.callscreen.icalldialer.Utils.FavoriteContactRemoverItemTask;
import com.icaller.callscreen.icalldialer.Utils.Permission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class AllListDataFragment extends Fragment {
    private static  int REQUEST_READ_CALL_LOG = 1;
    private CallLogAllAdapter callLogAdapter;
    CustomLoadProgressDialogView customProgressDialog;
    private boolean data;
    LinearLayout nodata;
    ImageView nodataimage;
    private RecyclerView recyclerView;
    int missedCallCount = 0;
    Map<String, Integer> missedCallCounts = new HashMap();
    String[] permissons = {"android.permission.READ_CONTACTS", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CONTACTS", "android.permission.READ_CONTACTS", "android.permission.READ_PHONE_STATE"};

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public AllListDataFragment(boolean z) {
        this.data = z;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_all_list, viewGroup, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.customProgressDialog = new CustomLoadProgressDialogView(getActivity());
        this.nodata = (LinearLayout) inflate.findViewById(R.id.nodata);
        this.nodataimage = (ImageView) inflate.findViewById(R.id.nodataimage);
        if (!Permission.isPhonenGranted(getActivity())) {
            Permission.getPhonePermission(getActivity(), 1234);
        }
        if (Permission.isPhonenGranted(getActivity())) {
            fetchCallLogs();
        } else {
            Log.e("TAG", "onCreateView:********** ");
            RequestPermission(getActivity(), this.permissons, 1);
        }
        if (getActivity().getSharedPreferences(getActivity().getPackageName(), 0).getBoolean("dark_theme", false)) {
            this.nodataimage.setImageResource(R.drawable.favourite);
            getActivity().setTheme(R.style.AppThemeContactContact1);
        } else {
            this.nodataimage.setImageResource(R.drawable.favourite);
            getActivity().setTheme(R.style.AppThemeContactContact);
        }
        Log.e("TAG", "onCreateView:***************************** " + this.data);
        if (this.data) {
            fetchCallLogs();
            this.callLogAdapter.notifyDataSetChanged();
        }
        new SwipeToDeleteDataCallbackContact(getActivity(), this.recyclerView) {
            @Override
            public void instantiateUnderlayButton( RecyclerView.ViewHolder viewHolder, List<SwipeToDeleteDataCallbackContact.UnderlayButton> list) {
                list.add(new SwipeToDeleteDataCallbackContact.UnderlayButton(AllListDataFragment.this.getActivity(), "", R.drawable.delete_btn, Color.parseColor("#FF3C30"), new SwipeToDeleteDataCallbackContact.UnderlayButtonClickListener() {
                    @Override
                    public void onClick(int i) {
                        String number = AllListDataFragment.this.callLogAdapter.callLogs.get(viewHolder.getAdapterPosition()).getNumber();
                        Log.e("TAG", "onClick:***************** " + number);
                        AllListDataFragment.this.callLogAdapter.removeItem(viewHolder.getAdapterPosition());
                        FavoriteContactRemoverItemTask.removeRecentCallLogs(AllListDataFragment.this.getActivity().getContentResolver(), number);
                        if (AllListDataFragment.this.callLogAdapter.callLogs.size() == 0) {
                            AllListDataFragment.this.nodata.setVisibility(0);
                            AllListDataFragment.this.recyclerView.setVisibility(8);
                        } else {
                            AllListDataFragment.this.nodata.setVisibility(8);
                            AllListDataFragment.this.recyclerView.setVisibility(0);
                        }
                        Toast.makeText(AllListDataFragment.this.getActivity(), "Delete Contact", 0).show();
                    }
                }));
            }
        }.attachSwipe();
        return inflate;
    }

    public void RequestPermission(Activity activity, String[] strArr, int i) {
        if (ContextCompat.checkSelfPermission(activity, strArr[0]) == 0 || ActivityCompat.shouldShowRequestPermissionRationale(activity, strArr[0])) {
            return;
        }
        ActivityCompat.requestPermissions(activity, strArr, i);
    }

    public boolean CheckPermission(Context context, String str) {
        return ContextCompat.checkSelfPermission(context, str) == 0;
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
        if (i == 1) {
            if (iArr.length > 0 && iArr[0] == 0) {
                Toast.makeText(getActivity(), "Permission Granted", 0).show();
            } else {
                Toast.makeText(getActivity(), "Permission denied, cannot make direct calls.", 0).show();
            }
        }
    }

    private void fetchCallLogs() {
        Cursor query;
        ArrayList arrayList = new ArrayList();
        if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_CALL_LOG") == 0 && (query = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, "date DESC")) != null) {
            int columnIndex = query.getColumnIndex("number");
            int columnIndex2 = query.getColumnIndex("date");
            int columnIndex3 = query.getColumnIndex("type");
            int columnIndex4 = query.getColumnIndex("name");
            HashMap hashMap = new HashMap();
            while (query.moveToNext()) {
                this.customProgressDialog.show();
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
                    if (string != null) {
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
        CallLogAllAdapter callLogAllAdapter = new CallLogAllAdapter(uniqueCallLogs, getActivity());
        this.callLogAdapter = callLogAllAdapter;
        this.recyclerView.setAdapter(callLogAllAdapter);
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
