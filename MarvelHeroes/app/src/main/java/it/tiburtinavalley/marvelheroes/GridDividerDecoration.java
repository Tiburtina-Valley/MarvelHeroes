package it.tiburtinavalley.marvelheroes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;

import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class GridDividerDecoration extends RecyclerView.ItemDecoration {

    private final Paint dividerPaint;

    private final float dividerHeight;

    public GridDividerDecoration(Context context) {
        this.dividerPaint = new Paint();
        this.dividerPaint.setColor(ContextCompat.getColor(context, R.color.dividerColor));
        this.dividerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, context.getResources().getDisplayMetrics());
    }

    @Override
    public void onDraw(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {

        final SectionedRecyclerViewAdapter adapter = (SectionedRecyclerViewAdapter) parent.getAdapter();

        for (int i = 0; i < parent.getChildCount(); i++) {

            final View child = parent.getChildAt(i);

            final int adapterPosition = parent.getChildAdapterPosition(child);

            assert adapter != null;
            if (adapterPosition != RecyclerView.NO_POSITION && i != 0 && adapter.isSubheaderAtPosition(adapterPosition)) {
                c.drawRect(child.getLeft(), child.getTop(), child.getRight(), child.getTop() + dividerHeight, dividerPaint);
            }

        }

    }


}
