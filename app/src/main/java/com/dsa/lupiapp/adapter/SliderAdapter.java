package com.dsa.lupiapp.adapter;

import static com.dsa.lupiapp.R.*;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsa.lupiapp.R;
import com.dsa.lupiapp.databinding.ImageSliderLayoutItemBinding;
import com.dsa.lupiapp.entity.service.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private Context context;
    private List<SliderItem> sliderItems = new ArrayList<>();
    public SliderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        ImageSliderLayoutItemBinding view = ImageSliderLayoutItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {

        SliderItem sliderItem = sliderItems.get(position);

        viewHolder.binding.tvAutoImageSlider.setText(sliderItem.getTitulo());
        viewHolder.binding.tvAutoImageSlider.setTextSize(16);
        viewHolder.binding.tvAutoImageSlider.setTextColor(Color.WHITE);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImagen())
                .fitCenter()
                .into(viewHolder.binding.ivAutoImageSlider);

        /*viewHolder.textView.setText(sliderItem.getTitulo());
        viewHolder.textView.setTextSize(16);
        viewHolder.textView.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImagen())
                .fitCenter()
                .into(viewHolder.imageView);*/

    }

    @Override
    public int getCount() {
        return sliderItems.size();
    }

    public void updateItem(List<SliderItem> list) {
        sliderItems.clear();
        sliderItems.addAll(list);
        this.notifyDataSetChanged();
    }

    protected class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {

        private ImageSliderLayoutItemBinding binding;

        public SliderAdapterViewHolder(ImageSliderLayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            /*imageView = itemView.findViewById(R.id.iv_auto_image_slider);
            textView = itemView.findViewById(R.id.tv_auto_image_slider);
            this.itemView = itemView;*/
        }
    }

}
