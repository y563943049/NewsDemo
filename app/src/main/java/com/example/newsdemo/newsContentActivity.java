package com.example.newsdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class newsContentActivity extends AppCompatActivity {

    public static void actionStart(Context context,String newsTitle,String newsContent,int imageId){
        Intent intent = new Intent(context,newsContentActivity.class);
        intent.putExtra("newsTitle",newsTitle);
        intent.putExtra("newsContent",newsContent);
        intent.putExtra("newsImageId",imageId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_content);
        Intent intent = getIntent();
        String newsTitle = intent.getStringExtra("newsTitle");
        String newsContent = intent.getStringExtra("newsContent");
        int imageId = intent.getIntExtra("newsImageId",0);
        NewsContentFragment newsContentFragment = (NewsContentFragment) getSupportFragmentManager().findFragmentById(R.id.news_content_fragment);
        newsContentFragment.refresh(newsTitle,newsContent,imageId);
    }

}
