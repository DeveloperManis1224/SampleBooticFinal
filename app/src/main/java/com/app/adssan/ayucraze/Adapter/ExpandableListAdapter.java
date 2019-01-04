package com.app.adssan.ayucraze.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.adssan.ayucraze.R;
import com.app.adssan.ayucraze.model.MenuModel;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).getName();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        ImageView img = convertView.findViewById(R.id.icon_nav);

        img.setImageDrawable(getGroup(groupPosition).getImage());
        ImageView imgdrop = convertView.findViewById(R.id.icon_drop);
//
        if(getGroup(groupPosition).getName().equalsIgnoreCase("Nairjara Silks"))
        {
            lblListHeader.setTextColor(convertView.getResources().getColor(R.color.color_dialog_white));
            ((LinearLayout)convertView.findViewById(R.id.lyt_group_header)).setBackgroundColor(convertView.getResources().getColor(R.color.colorPrimary));
        }
        else  if(getGroup(groupPosition).getName().equalsIgnoreCase("Home"))
        {
            lblListHeader.setTextColor(convertView.getResources().getColor(R.color.color_dialog_black));
            ((LinearLayout)convertView.findViewById(R.id.lyt_group_header)).setBackgroundColor(convertView.getResources().getColor(R.color.color_dialog_white));
            Log.e("XSXSXSXS",""+getGroup(groupPosition).getName());
        }
//        imgdrop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(context, "Click Down Icon", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
