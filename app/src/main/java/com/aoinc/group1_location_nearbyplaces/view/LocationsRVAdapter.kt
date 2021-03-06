package com.aoinc.group1_location_nearbyplaces.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aoinc.group1_location_nearbyplaces.R
import com.aoinc.group1_location_nearbyplaces.model.data.NearbySearchResponse
import com.aoinc.group1_location_nearbyplaces.view.LocationsRVAdapter.ResultsItemViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView

class LocationsRVAdapter(
    private var locationList: List<NearbySearchResponse.Result>,
    private val locationListDelegate: LocationListDelegate
    )
    : RecyclerView.Adapter<ResultsItemViewHolder>() {

    fun updateLocationList(newLocations: List<NearbySearchResponse.Result>) {
        locationList = newLocations
        notifyDataSetChanged()
    }

    interface LocationListDelegate {
        fun onListItemSelected(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsItemViewHolder =
        ResultsItemViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.location_list_item, parent, false), locationListDelegate)

    override fun onBindViewHolder(holder: ResultsItemViewHolder, position: Int) {
        val location: NearbySearchResponse.Result = locationList[position]

        holder.apply {

            // photos variable may be unset if Places API did not return anything for it
            location.placesPhotos?.let {
                Glide.with(holder.itemView.context)
                    .load(it[0].placesPhotoReference)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .into(locationPhoto)
            }

            locationName.text = location.placesName
            locationRating.rating = location.placesRating.toFloat()
        }
    }

    override fun getItemCount(): Int = locationList.size

    inner class ResultsItemViewHolder(
        itemView: View,
        private val locationListDelegate: LocationListDelegate
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val locationPhoto: ImageView = itemView.findViewById(R.id.item_photo_imageView)
        val locationName: MaterialTextView = itemView.findViewById(R.id.item_title_textView)
        val locationRating: RatingBar = itemView.findViewById(R.id.item_rating_bar)

//        @SuppressLint("NewApi")
//        var backgroundColor: Int = itemView.context.getColor(R.color.white)

        override fun onClick(v: View?) {
            locationListDelegate.onListItemSelected(adapterPosition)
//            Log.d("RV_CLICK", "at onClick in RV")
        }

    }
}