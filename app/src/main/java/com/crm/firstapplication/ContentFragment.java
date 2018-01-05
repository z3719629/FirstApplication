package com.crm.firstapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * TODO
 * @author cuiran
 * @version 1.0.0
 */
public class ContentFragment extends Fragment {

    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_content, container, false);
        textView = (TextView) view.findViewById(R.id.textView);

        String text = getArguments().getString("text");
        textView.setText(text);

        return view;
    }

}