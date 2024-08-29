package teamyj.dev.hrd_final_project.layout;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import teamyj.dev.hrd_final_project.R;

public class DruglistAdapter extends SimpleCursorAdapter {

    private LayoutInflater inflater;

    public DruglistAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags) {
        super(context, layout, cursor, from, to, flags);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = view.findViewById(R.id.nameTextView);
//        TextView colorTextView = view.findViewById(R.id.colorTextView);
//        TextView shapeTextView = view.findViewById(R.id.shapeTextView);

        // DB에서 가져온 데이터 설정
        String name = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
//        String color = cursor.getString(cursor.getColumnIndexOrThrow("color"));
//        String shape = cursor.getString(cursor.getColumnIndexOrThrow("shape"));

        nameTextView.setText("Name: " + name);
//        colorTextView.setText("Color: " + color);
//        shapeTextView.setText("Shape: " + shape);

    }
}