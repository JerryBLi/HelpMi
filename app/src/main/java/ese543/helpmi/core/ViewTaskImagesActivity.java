package ese543.helpmi.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

import ese543.helpmi.R;

public class ViewTaskImagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_images);

        Intent intent = getIntent();
        ArrayList<Uri> uris = intent.getParcelableArrayListExtra("taskImages");
        GridView gridView = findViewById(R.id.gridViewImages);
        ImageAdapter ia = new ImageAdapter(this,uris);
        gridView.setAdapter(ia);
    }

    class ImageAdapter extends BaseAdapter {

        private ArrayList<Uri> uris;
        Context c;
        public ImageAdapter(Context c, ArrayList<Uri> uris)
        {
            this.uris = uris;
            this.c = c;
        }
        public int getCount() {
            return uris.size();
        }
        public Object getItem(int position) {
            return uris.get(position);
        }
        public long getItemId(int position) {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView iv = new ImageView(c);
            iv.setImageURI(uris.get(position));
            return iv;
        }


    }
}
