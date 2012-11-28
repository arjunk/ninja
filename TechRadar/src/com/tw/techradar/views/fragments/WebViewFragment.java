package com.tw.techradar.views.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.tw.techradar.R;

public class WebViewFragment extends Fragment {
    String URL;

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL = getArguments() != null ? getArguments().getString("URL") : "";
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.web_view, container, false);
        WebView webView = (WebView) v.findViewById(R.id.htmlView);
        webView.loadUrl(URL);
        return v;
    }
}
