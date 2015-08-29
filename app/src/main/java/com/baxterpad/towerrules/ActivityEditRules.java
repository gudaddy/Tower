package com.baxterpad.towerrules;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sean on 8/29/2015.
 */
public class ActivityEditRules extends ActivityHideSystemUI{


    private ListView ruleEditView;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rules);



        ruleEditView = (ListView) findViewById(R.id.ruleEditView);
        String[] rules = Rules.getRuleList();

        ArrayList<String> rulesList = new ArrayList<String>();
        rulesList.addAll(Arrays.asList(rules));

        listAdapter = new ArrayAdapter<String>(this,R.layout.row_test_view,rulesList);
        ruleEditView.setAdapter(listAdapter);
    }

}
