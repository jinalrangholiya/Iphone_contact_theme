package com.icaller.callscreen.icalldialer.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.icaller.callscreen.icalldialer.R;
import com.icaller.callscreen.icalldialer.Model.ContactFavList;
import com.icaller.callscreen.icalldialer.Utils.FavoriteContactRemoverItemTask;

import java.util.List;


public class ContactFavriteAdapter extends RecyclerView.Adapter<ContactFavriteAdapter.ViewHolder> {
    public List<ContactFavList> contacts;
    Context context;
    public boolean result;

    public ContactFavriteAdapter(List<ContactFavList> list, Context context, boolean z) {
        this.contacts = list;
        this.context = context;
        this.result = z;
    }

    @Override 
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_item, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView")  int i) {
         ContactFavList contactFav = this.contacts.get(i);
        viewHolder.name.setText(contactFav.getName());
        Log.e("TAG", "onBindViewHolder: " + contactFav.getPhoneNumber());
        viewHolder.number.setText(contactFav.getPhoneNumber());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ContactFavriteAdapter.this.makeDirectCall(contactFav.getPhoneNumber());
            }
        });
        String[] split = contactFav.getName().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String str : split) {
            if (!str.isEmpty()) {
                sb.append(Character.toUpperCase(str.charAt(0)));
            }
        }
        viewHolder.shortname.setText(sb.toString());
        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ContactFavriteAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(ContactFavriteAdapter.this.contacts.get(i).getId()))));
            }
        });
        if (this.result) {
            viewHolder.shortname.setVisibility(8);
            viewHolder.deleteicon.setVisibility(0);
        } else {
            viewHolder.shortname.setVisibility(0);
            viewHolder.deleteicon.setVisibility(8);
        }
        viewHolder.deleteicon.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ContactFavriteAdapter.this.removeItem(i);
                ContentResolver contentResolver = ContactFavriteAdapter.this.context.getContentResolver();
                String contactID = FavoriteContactRemoverItemTask.getContactID(contentResolver, contactFav.getPhoneNumber(), contactFav.getName());
                if (contactID != null) {
                    FavoriteContactRemoverItemTask.removeFromFavorites(contentResolver, contactID);
                }
                ContactFavriteAdapter.this.notifyDataSetChanged();
            }
        });
        Context context = this.context;
        if (context.getSharedPreferences(context.getPackageName(), 0).getBoolean("dark_theme", false)) {
            viewHolder.line.setBackgroundResource(R.color.darkline);
            viewHolder.shortname.setBackgroundResource(R.drawable.bg_round_dark);
            this.context.setTheme(R.style.AppThemeContactContact1);
            return;
        }
        viewHolder.line.setBackgroundResource(R.color.lightline);
        viewHolder.shortname.setBackgroundResource(R.drawable.bg_rounded);
        this.context.setTheme(R.style.AppThemeContactContact);
    }

    public void makeDirectCall(String str) {
        if (ContextCompat.checkSelfPermission(this.context, "android.permission.CALL_PHONE") != 0) {
            Toast.makeText(this.context, "Please Permission Allow", 0).show();
        } else {
            startDirectCall(str);
        }
    }

    public void setEditMode(boolean z) {
        this.result = z;
        notifyDataSetChanged();
    }

    public void removeItem(int i) {
        this.contacts.remove(i);
        notifyItemRemoved(i);
    }

    private void startDirectCall(String str) {
        this.context.startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + str)));
    }

    @Override 
    public int getItemCount() {
        return this.contacts.size();
    }

    public void updateContacts(List<ContactFavList> list) {
        this.contacts.clear();
        this.contacts.addAll(list);
        notifyDataSetChanged();
    }

    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView deleteicon;
        public ImageView image;
        View line;
        public TextView name;
        public TextView number;
        TextView shortname;

        public ViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name);
            this.number = (TextView) view.findViewById(R.id.number);
            this.image = (ImageView) view.findViewById(R.id.info);
            this.shortname = (TextView) view.findViewById(R.id.shortname);
            this.deleteicon = (ImageView) view.findViewById(R.id.delteicon);
            this.line = view.findViewById(R.id.line);
        }
    }
}
