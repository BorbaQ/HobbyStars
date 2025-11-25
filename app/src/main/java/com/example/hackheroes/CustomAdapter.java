package com.example.hackheroes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Star> items = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText nameInput;
        EditText descInput;
        SeekBar positionSlider;
        Button delButton;

        public ViewHolder(View itemView) {
            super(itemView);

            nameInput = itemView.findViewById(R.id.starNameInput);
            descInput = itemView.findViewById(R.id.starDescInput);
            positionSlider = itemView.findViewById(R.id.positionSlider);
            delButton = itemView.findViewById(R.id.delButton);

            positionSlider.setMax(80);
        }

        public void bind(Star item) {
            nameInput.setText(item.name);
            descInput.setText(item.description);


            int sliderProgress = (int) ((item.position - 0.1f) * 100f);
            positionSlider.setProgress(sliderProgress);
        }
    }

    public CustomAdapter(ArrayList<Star> stars) {
        this.items = stars;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.star, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Star item = items.get(position);

        holder.delButton.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                items.remove(pos);
                notifyItemRemoved(pos);
            }
        });


        holder.bind(item);

        holder.nameInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.name = s.toString();
            }
        });

        holder.descInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                item.description = s.toString();
            }
        });

        holder.positionSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                item.position = 0.1f + (progress / 100f);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem() {
        items.add(new Star("","",false,0.5f));
        notifyItemInserted(items.size() - 1);
    }

    public ArrayList<Star> getAllItems() {
        return items;
    }
}
