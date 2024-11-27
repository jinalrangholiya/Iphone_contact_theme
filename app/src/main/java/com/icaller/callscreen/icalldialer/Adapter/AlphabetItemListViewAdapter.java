package com.icaller.callscreen.icalldialer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Fragment.ContactsListActivity;

import java.util.ArrayList;
import java.util.List;


public class AlphabetItemListViewAdapter extends BaseAdapter implements Filterable {
    ContactsListActivity contactsActivity;
    private List<Row> rows;
    private List<Row> tempRows;

    @Override 
    public long getItemId(int i) {
        return i;
    }

    @Override 
    public int getViewTypeCount() {
        return 2;
    }

    public void setRows(List<Row> list) {
        this.rows = list;
        this.tempRows = list;
    }

    
    public static abstract class Row {
        public  String text;

        protected Row(String str) {
            this.text = str;
        }
    }

    public AlphabetItemListViewAdapter(ContactsListActivity contactsActivity) {
        this.contactsActivity = contactsActivity;
    }

    
    public static  class Header extends Row {
        public Header(String str) {
            super(str);
        }
    }

    
    public static  class Cell extends Row {
        public Cell(String str) {
            super(str);
        }
    }

    @Override 
    public int getCount() {
        return this.rows.size();
    }

    @Override 
    public Row getItem(int i) {
        return this.rows.get(i);
    }

    @Override 
    public int getItemViewType(int i) {
        return getItem(i) instanceof Header ? 1 : 0;
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i) == 0) {
            if (view == null) {
                view = (LinearLayout) ((LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_contact_item_data, viewGroup, false);
            }
            Cell cell = (Cell) getItem(i);
            ((TextView) view.findViewById(R.id.textView1)).setText(cell.text);
            view.setTag(cell);
        } else {
            if (view == null) {
                view = (LinearLayout) ((LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_contact_section_item, viewGroup, false);
            }
            ((TextView) view.findViewById(R.id.textView1)).setText(((Header) getItem(i)).text);
        }
        return view;
    }

    @Override 
    public Filter getFilter() {
        return new Filter() { 
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                AlphabetItemListViewAdapter.this.rows = (List) filterResults.values;
                if (filterResults.count == 0) {
                    AlphabetItemListViewAdapter.this.contactsActivity.updatelayout(true);
                } else {
                    AlphabetItemListViewAdapter.this.contactsActivity.updatelayout(false);
                }
                AlphabetItemListViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                ArrayList arrayList = new ArrayList();
                if (charSequence == null || charSequence.length() == 0) {
                    filterResults.values = AlphabetItemListViewAdapter.this.tempRows;
                    filterResults.count = AlphabetItemListViewAdapter.this.tempRows.size();
                } else {
                    for (int i = 0; i < AlphabetItemListViewAdapter.this.tempRows.size(); i++) {
                        Row row = (Row) AlphabetItemListViewAdapter.this.tempRows.get(i);
                        if (row instanceof Header) {
                            if (((Header) row).text.toLowerCase().substring(0, 1).equals(charSequence.toString().toLowerCase().substring(0, 1))) {
                                arrayList.add(row);
                            }
                        } else if ((row instanceof Cell) && ((Cell) row).text.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                            arrayList.add(row);
                        }
                    }
                    if (arrayList.size() <= 1) {
                        arrayList.clear();
                    }
                    filterResults.values = arrayList;
                    filterResults.count = arrayList.size();
                }
                return filterResults;
            }
        };
    }
}
