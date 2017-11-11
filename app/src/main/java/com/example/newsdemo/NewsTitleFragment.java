package com.example.newsdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * @version $Rev$
 * @anthor Administrator
 * @dsc ${TOOD}
 * @updateAuthor $Author
 * @updateDsc ${TOOD}
 */
public class NewsTitleFragment extends Fragment {

    private HttpUtil mHttpUtil;

    private ImageView image;

    private boolean isTwoPlne;

    private List<News> mNewsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_frag,container,false);
        RecyclerView newsTitleRecyclerView = (RecyclerView) view.findViewById(R.id.news_title_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        newsTitleRecyclerView.setLayoutManager(manager);
        getNews();
        NewsAdapter adapter = new NewsAdapter(mNewsList);
        newsTitleRecyclerView.setAdapter(adapter);
        return view;
    }

    private void getNews() {
        mHttpUtil.sendRequestWithOkhttp("http://192.168.0.101:8080/webServer/newslist.xml", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.i(TAG, "onResponse: 进入网站失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: 成功进入网站！");
                final String responseData = response.body().string();
                Log.i(TAG, "onResponse: "+responseData);
                parseXMLWithPull(responseData);
            }
        });


    }

    private void parseXMLWithPull(String responseData) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(responseData));
            Log.i(TAG, "parseXMLWithPull: "+responseData);
            int eventType = parser.getEventType();
            Log.i(TAG, "parseXMLWithPull: "+eventType);
            String title = "";
            String description = "";
            String image = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = parser.getName();
                Log.i(TAG, "parseXMLWithPull: "+nodeName);
                switch (eventType){
                    case XmlPullParser.START_TAG: {
                        if("title".equals(nodeName)){
                            title = parser.nextText();
                            Log.i(TAG, "title:" + title);
                        }else if("description".equals(nodeName)){
                            description = parser.nextText();
                        }else if("image".equals(nodeName)){
                            image = parser.nextText();
                        }
                        break;
                    }
                    case XmlPullParser.END_TAG:{
                        if ("app".equals(nodeName)){
                            News news = new News();
                            news.setTitle(title);
                            news.setContent(description);
                            news.setImageId(R.drawable.leimur);
                            mNewsList.add(news);
                        }
                        break;
                    }
                    default:
                        break;
                }

                Log.i(TAG, "parseXMLWithPull: "+eventType);
                eventType = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().findViewById(R.id.news_content_layout) != null){
            //双页模式
            isTwoPlne = true;
        }else {
            //单页模式
            isTwoPlne = false;
        }
    }
    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


        private List<News> mNewses;


        public NewsAdapter(List<News> news){
            this.mNewses = news;
        }


        class ViewHolder extends RecyclerView.ViewHolder{

            private TextView newsTitle;
            //private TextView newsContent;
            //private ImageView newsImage;

            public ViewHolder(View itemView) {
                super(itemView);
                newsTitle = (TextView) itemView.findViewById(R.id.news_title);
                image = (ImageView) itemView.findViewById(R.id.news_image);
            }
        }
        @Override
        public NewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item,parent,false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    News news = mNewses.get(holder.getAdapterPosition());
                    if(isTwoPlne){
                        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(),news.getContent(),news.getImageId());
                    }else {
                        newsContentActivity.actionStart(getActivity(),news.getTitle(),news.getContent(),news.getImageId());
                        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new NotificationCompat.Builder(getActivity())
                                .setWhen(System.currentTimeMillis())
                                .setContentText("you click news")
                                .setContentTitle("News notification")
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .build();
                        notificationManager.notify(1,notification);
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
            News news = mNewses.get(position);
            holder.newsTitle.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewses.size();
        }
    }

}
