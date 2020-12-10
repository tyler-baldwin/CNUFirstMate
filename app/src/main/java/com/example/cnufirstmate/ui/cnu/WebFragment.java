package com.example.cnufirstmate.ui.cnu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.cnufirstmate.R;

public class WebFragment extends Fragment {

    private Web_ViewModel webViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        webViewModel =
                ViewModelProviders.of(this).get(Web_ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        webViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        WebView wv = root.findViewById(R.id.fragment_webview);
        wv.getSettings().setJavaScriptEnabled(true);

        wv.setWebViewClient(new WebViewClient());

        wv.loadUrl("https://cnu.edu");
        return root;
    }
}
