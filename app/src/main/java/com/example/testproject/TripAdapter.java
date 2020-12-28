package com.example.testproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.api.model.trip.Hotel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TripAdapter extends ArrayAdapter<Hotel> {

    private final String TAG="HotelAdapter";
    private final LayoutInflater inflater;
    private final int layout;
    private final List<Hotel> hotels;

    public TripAdapter(Context context, int resource, List<Hotel> hotels) {
        super(context, resource, hotels);
        this.hotels = hotels;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        convertView = inflater.inflate(R.layout.item_list_trips, null);

        final Hotel hotel = hotels.get(position);

        View.OnClickListener OCL_browser= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri website=Uri.parse(hotels.get(position).getUrl());
                Intent openWebsite = new Intent(Intent.ACTION_VIEW, website).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(openWebsite);
                final Animation animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);
                v.startAnimation(animScale);
            }
        };
        final View finalConvertView = convertView;
        View.OnClickListener OCL_delete= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animChildMove = AnimationUtils.loadAnimation(getContext(), R.anim.anim_move);
                finalConvertView.startAnimation(animChildMove);
                animChildMove.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.d("Child","START");
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.d("Child","END");
                        finalConvertView.setVisibility(View.INVISIBLE);
                        Log.d("count child",String.valueOf(parent.getChildCount()));
                        Log.d("position", String.valueOf(position));
                        hotels.remove(hotel);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

            }
        };

        String url = null;
        try {
            Picasso.get().load(hotel.getPhotos().getPhoto()).resize(parent.getWidth(), parent.getWidth()).centerCrop()
                    .into((ImageView)convertView.findViewById(R.id.image_hotel));
        } catch (Exception e) {
            Log.e(TAG, "Could not load image from: " + url);
//            hotels.remove(hotel);
//            notifyDataSetChanged();
        }
        ((TextView)convertView.findViewById(R.id.text_item_main)).setText(hotel.getTitle());
        ((TextView)convertView.findViewById(R.id.text_item_main)).setTextSize(20);
        ((LinearLayout)convertView.findViewById(R.id.LL_hotel_delete)).setOnClickListener(OCL_delete);
        ((LinearLayout)convertView.findViewById(R.id.LL_hotel_browser)).setOnClickListener( OCL_browser);
        return convertView;
    }


//    public View getView(final int position, final View convertView, ViewGroup parent) {
//
//        View view=inflater.inflate(this.layout, parent, false);
//
//        final Hotel hotel = hotels.get(position);
//
//        ImageView imageHotel = (ImageView) view.findViewById(R.id.image_hotel);
//        TextView nameView = (TextView) view.findViewById(R.id.text_item_main);
//        LinearLayout LL_hotel_delete;
//        LinearLayout LL_hotel_browser;
//
//        Button btnDelete=(Button) view.findViewById(R.id.btndelete);
//        Button btnToBrowser=(Button) view.findViewById(R.id.btntobrower);
//
//        View.OnClickListener OCL_browser= new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri website=Uri.parse(hotels.get(position).getUrl());
//                Intent openWebsite = new Intent(Intent.ACTION_VIEW, website).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getContext().startActivity(openWebsite);
//            }
//        };
//        View.OnClickListener OCL_delete= new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hotels.remove(hotel);
//                notifyDataSetChanged();
//            }
//        };
//        LL_hotel_browser=view.findViewById(R.id.LL_hotel_browser);
//        LL_hotel_delete=view.findViewById(R.id.LL_hotel_delete);
//
//        LL_hotel_browser.setOnClickListener( OCL_browser);
//        LL_hotel_delete.setOnClickListener(OCL_delete);
//        btnDelete.setOnClickListener(OCL_delete);
//        btnToBrowser.setOnClickListener(OCL_browser);
//
//        String url = null;
//        try {
//            Picasso.get().load(hotel.getPhotos().getPhoto()).resize(parent.getWidth(), parent.getWidth()).centerCrop().into(imageHotel);
//        } catch (Exception e) {
//            Log.e(TAG, "Could not load image from: " + url);
//            hotels.remove(hotel);
//            notifyDataSetChanged();
//        }
//        nameView.setText(hotel.toString());
//
//
//        return view;
//    }
}
