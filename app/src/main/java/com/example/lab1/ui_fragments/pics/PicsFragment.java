package com.example.lab1.ui_fragments.pics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.lab1.R;
import com.example.lab1.adapters.PicsAdapter;
import com.example.lab1.model.PicItem;
import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PicsFragment extends Fragment {

    private static final int RESULT_LOAD_IMG = 1;
    private ListAdapter adapter;
    private AsymmetricGridView listView;
    final List<PicItem> items = new ArrayList<>();
    private int index = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pics, container, false);

        listView = root.findViewById(R.id.picsList);
        Button addButton = root.findViewById(R.id.addPicButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });


        listView.setRequestedColumnWidth(Utils.dpToPx(this.getContext(), 120));
        adapter = new PicsAdapter(getContext(), items);
        listView.setAdapter(new AsymmetricGridViewAdapter(getContext(), listView, adapter));

        return root;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        System.out.println("Result Code: " + reqCode);
        if (reqCode == RESULT_LOAD_IMG) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                ArrayList<Integer> Span = new ArrayList<>();
                fillSpan(Span);
                items.add(new PicItem(Span.get(index % 9), Span.get(index % 9), 0, selectedImage, imageUri));
                index++;
                refresh();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void fillSpan(ArrayList<Integer> columnSpan) {
        columnSpan.add(2);
        for (int i = 0; i < 7; i++) {
            columnSpan.add(1);
        }
        columnSpan.add(2);
    }

    private void refresh() {
        listView.setRequestedColumnWidth(Utils.dpToPx(this.getContext(), 120));
        adapter = new PicsAdapter(getContext(), items);
        listView.setAdapter(new AsymmetricGridViewAdapter(getContext(), listView, adapter));
    }
}
