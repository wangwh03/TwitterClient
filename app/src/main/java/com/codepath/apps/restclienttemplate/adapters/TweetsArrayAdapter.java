package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.RelativeTimestampParser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wewang on 11/7/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    public interface OnClickProfileListener {
        void onProfileImageClick(String screenName);
    }

    private OnClickProfileListener listener;

    private static class ViewHolder {
        ImageView ivUserProfileImage;
        TextView tvName;
        TextView tvUserName;
        TextView tvBody;
        TextView tvRelativeTimeStamp;
    }

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

            viewHolder.ivUserProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUsername);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvRelativeTimeStamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvUserName.setText("@" + tweet.getUser().getScreenName());
//        viewHolder.ivUserProfileImage.setTag(tweet.getUser().getScreenName());
        viewHolder.tvBody.setText(Html.fromHtml(tweet.getBody()));

        viewHolder.ivUserProfileImage.setImageResource(android.R.color.transparent);
        viewHolder.ivUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProfileImageClick(tweet.getUser().getScreenName());
            }
        });

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivUserProfileImage);

        viewHolder.tvRelativeTimeStamp.setText(RelativeTimestampParser.getRelativeTimeAgo(tweet.getTimestamp()));
        return convertView;
    }

    public void setOnClickProfileListener(OnClickProfileListener listener) {
        this.listener = listener;
    }

}
