package com.ablanco.tonsofdamage.summoner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.MasteryDto;
import com.ablanco.teemo.model.staticdata.MasteryListDto;
import com.ablanco.teemo.model.staticdata.MasteryTreeDto;
import com.ablanco.teemo.model.staticdata.MasteryTreeItemDto;
import com.ablanco.teemo.model.staticdata.MasteryTreeListDto;
import com.ablanco.teemo.model.summoners.Mastery;
import com.ablanco.teemo.model.summoners.MasteryPage;
import com.ablanco.teemo.model.summoners.MasteryPages;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.MasteryListNameAdapter;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.ResourcesHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MasteriesActivity extends BaseActivity implements MasteryTreeDetailItem.MasteryRequestListener {

    public final static String EXTRA_SUMMONER_ID = "extra_summoner_id";

    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.tv_mastery_tree_config)
    TextView mTvMasteryTreeConfig;
    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.loading)
    View loading;
    @Bind(R.id.spinner_page_name)
    Spinner mSpinnerPageName;

    private long summonerId;
    private List<List<MasteryTreeListDto>> masteryTreeDtos = new ArrayList<>();
    private List<String> mMasteriesNames = new ArrayList<>();
    private Map<String, MasteryDto> masteryData = new HashMap<>();
    private List<MasteryPageProxyModel> mMasteryPages = new ArrayList<>();
    private ArrayAdapter<MasteryPageProxyModel> spinnerAdapter;
    private HashMap<Long, String> mMasteryConfigByPage = new HashMap<>();
    private Thread workerThread;
    private MasteryTreeDto mTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_masteries);

        ButterKnife.bind(this);

        mPager.setOffscreenPageLimit(2);
        mPager.setAdapter(new MasteryTreePagerAdapter());
        mTab.setupWithViewPager(mPager);

        summonerId = getIntent().getLongExtra(EXTRA_SUMMONER_ID, 0);
        spinnerAdapter = new MasteryListNameAdapter(this, mMasteryPages);
        mSpinnerPageName.setAdapter(spinnerAdapter);
        mSpinnerPageName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mMasteryPages.get(position) != null && mMasteryPages.get(position).getMasteries() != null){
                    if(workerThread != null && workerThread.isAlive()){
                        workerThread.interrupt();
                        workerThread = null;
                    }

                    if(mMasteryConfigByPage.get(mMasteryPages.get(position).getId()) != null){
                        mTvMasteryTreeConfig.setText(mMasteryConfigByPage.get(mMasteryPages.get(position).getId()));
                    }else {
                        workerThread = new Thread(new SummonerMasteriesCountTask(mTree, mMasteryPages.get(position).getMasteryPage()));
                        workerThread.start();
                    }

                    for (int i = 0; i < mPager.getChildCount(); i++) {
                        if(mPager.getChildAt(i) != null && mPager.getChildAt(i) instanceof MasteryTreeDetailItem){
                            ((MasteryTreeDetailItem) mPager.getChildAt(i)).setSummonerMasteries(mMasteryPages.get(position).getMasteryPage());
                        }
                    }
                }else {
                    mTvMasteryTreeConfig.setText("0 / 0 / 0");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (summonerId > 0) {
           loadData();
        } else {
            finish();
        }


    }

    private void loadData(){

        mMasteriesNames.clear();
        masteryData.clear();
        masteryTreeDtos.clear();
        mMasteryPages.clear();
        mMasteryConfigByPage.clear();

        Teemo.getInstance(this).getStaticDataHandler().getMasteries(SettingsHandler.getLanguage(this), null,
                new StaticAPIQueryParams.StaticQueryParamsBuilder()
                        .include(StaticAPIQueryParams.Masteries.image)
                        .include(StaticAPIQueryParams.Masteries.tree)
                        .include(StaticAPIQueryParams.Masteries.ranks)
                        .include(StaticAPIQueryParams.Masteries.masteryTree)
                        .build(), new ServiceResponseListener<MasteryListDto>() {
                    @Override
                    public void onResponse(MasteryListDto response) {
                        if (response.getTree().getCunning() != null && response.getTree().getFerocity() != null && response.getTree().getResolve() != null) {
                            masteryData.putAll(response.getData());
                            masteryTreeDtos.add(response.getTree().getFerocity());
                            masteryTreeDtos.add(response.getTree().getCunning());
                            masteryTreeDtos.add(response.getTree().getResolve());

                            mTree = response.getTree();

                            mMasteriesNames.add(ResourcesHandler.getInstance(MasteriesActivity.this).getResourceForKey("masteryFerocity"));
                            mMasteriesNames.add(ResourcesHandler.getInstance(MasteriesActivity.this).getResourceForKey("masteryCunning"));
                            mMasteriesNames.add(ResourcesHandler.getInstance(MasteriesActivity.this).getResourceForKey("masteryResolve"));

                            mPager.getAdapter().notifyDataSetChanged();

                            Teemo.getInstance(MasteriesActivity.this).getSummonersHandler().getSummonerMasteryPages(String.valueOf(summonerId), new ServiceResponseListener<MasteryPages>() {
                                @Override
                                public void onResponse(MasteryPages response) {
                                    loading.setVisibility(View.GONE);
                                    Collections.sort(response.getPages(), new Comparator<MasteryPage>() {
                                        @Override
                                        public int compare(MasteryPage lhs, MasteryPage rhs) {
                                            return lhs.getId().compareTo(rhs.getId());
                                        }
                                    });

                                    for (MasteryPage masteryPage : response.getPages()) {
                                        mMasteryPages.add(new MasteryPageProxyModel(masteryPage));
                                    }
                                    spinnerAdapter.notifyDataSetChanged();

                                    for (int i = 0; i < mMasteryPages.size(); i++) {
                                        if(mMasteryPages.get(i) != null && mMasteryPages.get(i).isCurrent() != null && mMasteryPages.get(i).isCurrent()){
                                            mSpinnerPageName.setSelection(i, false);
                                        }
                                    }

                                }

                                @Override
                                public void onError(TeemoException e) {
                                    showError();

                                }
                            });

                        } else {
                            showError();

                        }

                    }

                    @Override
                    public void onError(TeemoException e) {
                        showError();
                    }
                });


    }

    private void showError(){
        loading.setVisibility(View.GONE);
        ErrorUtils.showPersistentError(mPager, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_MASTERIES;
    }

    @Override
    public MasteryDto getMastery(int id) {
        return masteryData.get(String.valueOf(id));
    }

    class MasteryTreePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return masteryTreeDtos.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mMasteriesNames.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = new MasteryTreeDetailItem(MasteriesActivity.this, masteryTreeDtos.get(position), MasteriesActivity.this);
            container.addView(v);

            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class SummonerMasteriesCountTask implements Runnable{

        private final MasteryPage page;
        private final MasteryTreeDto tree;

        private StringBuilder mStringBuilder;

        public SummonerMasteriesCountTask(MasteryTreeDto tree, MasteryPage page){
            this.page = page;
            this.tree = tree;
            this.mStringBuilder = new StringBuilder();
        }

        @Override
        public void run() {
            mStringBuilder.append(getNumberOfMasteriesForTree(tree.getFerocity()));
            mStringBuilder.append(" / ");
            mStringBuilder.append(getNumberOfMasteriesForTree(tree.getCunning()));
            mStringBuilder.append(" / ");
            mStringBuilder.append(getNumberOfMasteriesForTree(tree.getResolve()));
            mMasteryConfigByPage.put(page.getId(), mStringBuilder.toString());
            mTvMasteryTreeConfig.post(new Runnable() {
                @Override
                public void run() {
                    mTvMasteryTreeConfig.setText(mStringBuilder.toString());
                }
            });

        }

        private int getNumberOfMasteriesForTree(List<MasteryTreeListDto> tree){
            int count = 0;
            for (MasteryTreeListDto masteryTreeListDto : tree) {
                for (MasteryTreeItemDto masteryTreeItemDto : masteryTreeListDto.getMasteryTreeItems()) {
                    if(masteryTreeItemDto != null){
                        count += contains(masteryTreeItemDto.getMasteryId());
                    }
                }
            }

            return count;
        }

        private int contains(int id){
            for (Mastery mastery : page.getMasteries()) {
                if(mastery != null && mastery.getId() == id) return mastery.getRank();
            }

            return 0;
        }
    }


}
