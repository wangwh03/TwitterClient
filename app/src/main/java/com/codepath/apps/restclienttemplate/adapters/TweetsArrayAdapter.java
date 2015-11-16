package com.codepath.apps.restclienttemplate.adapters;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.utils.OwnerHelper;
import com.codepath.apps.restclienttemplate.utils.RelativeTimestampParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

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
        TextView tvRetweetLabel;
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
            viewHolder.tvRetweetLabel = (TextView) convertView.findViewById(R.id.tvRetweetLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Tweet tweetToDisplay;
        if (tweet.getRetweetedOriginal() == null) {
            viewHolder.tvRetweetLabel.setText("");
            viewHolder.tvRetweetLabel.setHeight(0);
            tweetToDisplay = tweet;
        } else {
            String retweetedName;
            if (tweet.getUser().getRemoteId() == OwnerHelper.fetchCurrentOwner().getRemoteId()) {
                retweetedName = getContext().getString(R.string.retweet_label_you);
            } else {
                retweetedName = tweet.getUser().getName();
            }
            viewHolder.tvRetweetLabel.setText(getContext().getString(R.string.retweet_label).replace("{:name}",
                    retweetedName));
            tweetToDisplay = tweet.getRetweetedOriginal();
        }
        viewHolder.tvName.setText(tweetToDisplay.getUser().getName());
        viewHolder.tvUserName.setText("@" + tweetToDisplay.getUser().getScreenName());
        viewHolder.tvBody.setText(Html.fromHtml(tweetToDisplay.getBody()));

        viewHolder.ivUserProfileImage.setImageResource(android.R.color.transparent);
        viewHolder.ivUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProfileImageClick(tweetToDisplay.getUser().getScreenName());
            }
        });
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.GRAY)
                .cornerRadiusDp(5)
                .oval(false)
                .build();

        Picasso.with(getContext())
                .load(tweetToDisplay.getUser().getProfileImageUrl())
                .fit()
                .transform(transformation)
                .into(viewHolder.ivUserProfileImage);
        viewHolder.tvRelativeTimeStamp.setText(RelativeTimestampParser.getRelativeTimeAgo(tweetToDisplay.getTimestamp()));
        return convertView;
    }

    public void setOnClickProfileListener(OnClickProfileListener listener) {
        this.listener = listener;
    }

}
