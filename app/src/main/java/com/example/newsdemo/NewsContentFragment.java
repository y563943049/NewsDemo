package com.example.newsdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * @version $Rev$
 * @anthor Administrator
 * @dsc ${TOOD}
 * @updateAuthor $Author
 * @updateDsc ${TOOD}
 */
public class NewsContentFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_content_frag,container,false);
        return view;
    }

    public void refresh(String title,String content,int imageId){
        View visibilityLayout = (LinearLayout) view.findViewById(R.id.visibility_layout);
        visibilityLayout.setVisibility(View.VISIBLE);
        TextView newsTitle = (TextView) view.findViewById(R.id.news_title);
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        TextView newsContent = (TextView) view.findViewById(R.id.news_content);
        newsTitle.setText(title);
        newsContent.setText(content);
        Glide.with(MyApplication.getContext()).load(imageId).into(newsImage);
    }
}
