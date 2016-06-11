package com.ablanco.tonsofdamage.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.status.Incident;
import com.ablanco.teemo.model.status.Message;
import com.ablanco.teemo.model.status.Service;
import com.ablanco.teemo.model.status.ShardStatus;
import com.ablanco.teemo.model.status.Translation;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseAnimatedDialog;
import com.ablanco.tonsofdamage.handler.SettingsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Álvaro Blanco Cabrero on 11/6/16
 * TonsOfDamage
 */
public class ServerStatusDialog extends BaseAnimatedDialog {

    private final static String STATUS_ONLINE = "online";

    @Bind(R.id.list)
    ExpandableListView expandableListView;

    private List<Service> mServices = new ArrayList<>();

    private LayoutInflater inflater;

    public static DialogFragment newInstance() {
        return new ServerStatusDialog();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_server_status, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inflater = LayoutInflater.from(getActivity());

        Teemo.getInstance(getActivity()).getStatusHandler().getShardStatus(SettingsHandler.getRegion(getActivity()), new ServiceResponseListener<ShardStatus>() {
            @Override
            public void onResponse(ShardStatus response) {
                if(getActivity() != null){
                    mServices.addAll(response.getServices());
                    expandableListView.setAdapter(new ShardAdapter());
                }
            }

            @Override
            public void onError(TeemoException e) {
                dismiss();
            }
        });
    }

    @OnClick(R.id.bt_ok)
    public void dismissDialog() {
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class ShardAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return mServices.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mServices.get(groupPosition).getIncidents().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mServices.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mServices.get(groupPosition).getIncidents().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            ServiceHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_shard_service_header, parent, false);
                holder = new ServiceHolder();
                holder.tvServiceName = (TextView) convertView.findViewById(R.id.tv_service_name);
                holder.tvServiceNumIncidents = (TextView) convertView.findViewById(R.id.tv_num_incidents);
                holder.imgStatus = convertView.findViewById(R.id.img_service_status);

                convertView.setTag(holder);
            } else {
                holder = (ServiceHolder) convertView.getTag();
            }

            Service service = (Service) getGroup(groupPosition);

            holder.tvServiceName.setText(service.getName());
            holder.tvServiceNumIncidents.setText(String.format(Locale.getDefault(), "(%d)", service.getIncidents().size()));
            holder.imgStatus.setBackgroundResource(service.getStatus().equalsIgnoreCase(STATUS_ONLINE) ? R.drawable.status_online : R.drawable.status_offline);


            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            IncidentHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_shard_incident, parent, false);
                holder = new IncidentHolder();
                holder.tvIncident = (TextView) convertView.findViewById(R.id.tv_incident);
                holder.tvSeverity = (TextView) convertView.findViewById(R.id.tv_severity);

                convertView.setTag(holder);
            } else {
                holder = (IncidentHolder) convertView.getTag();
            }

            Incident incident = (Incident) getChild(groupPosition, childPosition);

            if(incident.getUpdates() != null && !incident.getUpdates().isEmpty()){
                Message m = incident.getUpdates().get(0);
                if(m != null){
                    holder.tvSeverity.setText(m.getSeverity());
                    holder.tvIncident.setText(getMessageForLanguage(m, SettingsHandler.getLanguage(getActivity())));
                }

            }else {
                holder.tvIncident.setText("");
                holder.tvSeverity.setText("");
            }




            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        class ServiceHolder {
            TextView tvServiceName;
            TextView tvServiceNumIncidents;
            View imgStatus;
        }

        class IncidentHolder {
            TextView tvIncident;
            TextView tvSeverity;
        }
    }

    private String getMessageForLanguage(Message m, String lang){
        if(m.getTranslations() != null && !m.getTranslations().isEmpty()){
            for (Translation translation : m.getTranslations()) {
                if(translation.getLocale().equals(lang)) return translation.getContent();
            }

        }
        return m.getContent();
    }



}


