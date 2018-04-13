package net.coding.mart.setting;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.R;
import net.coding.mart.json.Example;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.list_item_example)
public class ExampleListItem extends FrameLayout {

    @ViewById
    ImageView icon;

    @ViewById
    TextView name, money, time, role;

    public ExampleListItem(Context context) {
        super(context);
    }

    public ExampleListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //    public ExampleListItem(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }

    @AfterViews
    void initExampleListItem() {
        Example data = (Example) getTag();
        setData(data);
    }

    public void setData(Example data) {
        if (data == null) {
            return;
        }
        setTag(data);

        if (name != null) {
            name.setText(data.getTitle());
            money.setText(String.format("%s", data.getAmount()));
            time.setText(String.format("%s", data.getDuration()));
            role.setText(String.format("%s", data.getCharacter()));
        }
    }
}
