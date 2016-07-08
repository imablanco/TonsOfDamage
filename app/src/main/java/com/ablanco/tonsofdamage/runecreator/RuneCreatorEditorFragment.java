package com.ablanco.tonsofdamage.runecreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ablanco.teemo.model.staticdata.RuneDto;
import com.ablanco.tonsofdamage.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco on 03/07/2016.
 * TonsOfDamage
 */
public class RuneCreatorEditorFragment extends RuneCreatorBaseFragment {

    private List<RuneDto> mRunes = new ArrayList<>();

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.rune_editor_container)
    FrameLayout mRuneEditorContainer;
    @Bind(R.id.sliding_panel)
    SlidingPaneLayout mSlidingPanel;

    private int mStepCounter = 0;
    private View mCurrentStep;

    private List<Object> mSteps = new ArrayList<>();

    public static Fragment newInstance(RuneCreatorFlowListener listener){
        RuneCreatorBaseFragment f = new RuneCreatorEditorFragment();
        f.setRuneCreatorFlowListener(listener);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_rune_creator_editor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSlidingPanel.openPane();
        mSlidingPanel.setSliderFadeColor(ContextCompat.getColor(getActivity(), R.color.black_alpha));

        //create first step, always the same
        mSteps.add(new SingleChoiceStep("description", 5168, 9));
        showNextStep();

    }

    private void showNextStep(){
        Object step = mSteps.get(mStepCounter);

        if(step instanceof SingleChoiceStep){
            RuneStepTemplateSingle v = new RuneStepTemplateSingle(getActivity());
            v.setInfo(((SingleChoiceStep) step).getExplain(), ((SingleChoiceStep) step).getRuneId(), ((SingleChoiceStep) step).getRuneAmount());
            mRuneEditorContainer.addView(v);
            mStepCounter++;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.rune_creator_editor, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_undo){
            // TODO: 05/07/2016 do undo
            return true;
        }else{
            return super.onOptionsItemSelected(item);
        }
    }
}
