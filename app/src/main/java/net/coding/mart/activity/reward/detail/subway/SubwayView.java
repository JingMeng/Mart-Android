package net.coding.mart.activity.reward.detail.subway;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.coding.mart.LengthUtil;
import net.coding.mart.R;
import net.coding.mart.common.constant.Progress;
import net.coding.mart.json.reward.Metro;
import net.coding.mart.json.reward.Published;
import net.coding.mart.json.reward.Role;
import net.coding.mart.json.reward.Stage;

import java.util.List;

/**
 * Created by chenchao
 * 地铁图控件
 */
public class SubwayView extends FrameLayout {

    FrameLayout stageLayout;

    public SubwayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMetro(Metro metro, Published published) {
        if (published.isCustomVersion()) {
            setCustomMetro(metro);
        } else {
            setMetro(metro);
        }
    }

    private void setCustomMetro(Metro metro) {
        inflate(getContext(), R.layout.view_reward_detail_graph_custom, this);
        stageLayout = (FrameLayout) findViewById(R.id.stageLayout);
        stageLayout.removeAllViews();

        int[] pointIds = new int[]{
                R.id.point0,
                R.id.point2,
                R.id.point3,
                R.id.point4,
                R.id.point5
        };

        int[] textIds = new int[]{
                R.id.text0,
                R.id.text2,
                R.id.text3,
                R.id.text4,
                R.id.text5
        };

        int nowStagePos;
        Progress progress = Progress.id2Enum(metro.status);
        switch (progress) {
            case waitPay:
                nowStagePos = 0;
                break;
            case recruit:
                nowStagePos = 1;
                break;
            case doing:
                nowStagePos = 2;
                break;
            case warranty:
                nowStagePos = 3;
            default: // finish:
                nowStagePos = 4;
                break;
        }

        final int pickTextColor = 0xFF6BC027;
        for (int i = 0; i < pointIds.length; ++i) {
            View point = findViewById(pointIds[i]);
            if (i == nowStagePos) {
                point.setBackgroundResource(R.mipmap.ic_subway_point_now);
                TextView textView = (TextView) findViewById(textIds[i]);
                textView.setTextColor(pickTextColor);
            } else if (i < nowStagePos) {
                point.setBackgroundResource(R.mipmap.ic_subway_point_over);
            } else {
                point.setBackgroundResource(R.mipmap.ic_subway_future);
            }
        }

        if (progress == Progress.doing) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup subwayLayout = (ViewGroup) inflater.inflate(R.layout.subway_layout, stageLayout, false);
            stageLayout.addView(subwayLayout);

            List<Role> roles = metro.roles;
            int rolePos = 0;
            for (Role role : roles) {
                ViewGroup layoutLine = (ViewGroup) inflater.inflate(R.layout.subway_layout_line, subwayLayout, false);
                subwayLayout.addView(layoutLine);

                List<Stage> stages = role.stages;
                int randomColor = getRoleColor(rolePos, roles.size());
                ++rolePos;
                for (Stage stage : stages) {
                    View lineItem = inflater.inflate(R.layout.subway_layout_line_item, layoutLine, false);

                    TextView textView = (TextView) lineItem.findViewById(R.id.text);
                    textView.setText(stage.stageNo);

                    GradientDrawable drawable = (GradientDrawable) lineItem.getBackground();
                    int color;
                    if (stage.getStatusEnum().hightLight()) {
                        color = randomColor;
                    } else {
                        color = 0xffD7D8D7;
                    }

                    drawable.setColor(color);

                    layoutLine.addView(lineItem);
                }
            }
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (findViewById(R.id.rootLayout).getWidth() >= LengthUtil.getsWidthPix()) {
                    return;
                }

                int[] linesFull = new int[] {
                        R.id.line0,
                        R.id.line2
                };

                // line3 和 line4 都算半根
                int[] linesHalf = new int[] {
                        R.id.line3,
                        R.id.line4
                };

                resizeAllLines(linesFull, linesHalf);
            }
        });
    }

    private void resizeAllLines(int[] linesFull, int[] linesHalf) {
        int lineCount = linesFull.length + linesHalf.length / 2;

        int lineWidthSum = LengthUtil.getsWidthDp() - (15 + 25) * 2 - 10 * (lineCount + 1); // 屏幕宽度 - 内边距 * - 点宽度 * 4;

        if (lineWidthSum / lineCount < 53) { // 如果 line 长度少于 53dp, 就不重设长度了
            return;
        }

        int lineWidth = LengthUtil.dpToPx(lineWidthSum) / lineCount ;
        for (int lineId : linesFull) {
            resizeLine(lineWidth, lineId);
        }

        int lineHalfWidth = lineWidth / 2;
        for (int lineId : linesHalf) {
            resizeLine(lineHalfWidth, lineId);
        }
    }

    private void resizeLine(int lineWidth, int lineId) {
        View line = findViewById(lineId);
        ViewGroup.LayoutParams lp = line.getLayoutParams();
        lp.width = lineWidth;
        line.setLayoutParams(lp);
    }

    private void setMetro(Metro metro) {
        inflate(getContext(), R.layout.view_reward_detail_graph, this);
        stageLayout = (FrameLayout) findViewById(R.id.stageLayout);
        stageLayout.removeAllViews();

        int[] pointIds = new int[]{
                R.id.point0,
                R.id.point1,
                R.id.point2,
                R.id.point3,
                R.id.point4,
        };

        int[] textIds = new int[]{
                R.id.text0,
                R.id.text1,
                R.id.text2,
                R.id.text3,
                R.id.text4,
        };

        int nowStagePos;
        Progress progress = Progress.id2Enum(metro.status);
        switch (progress) {
            case waitCheck:
                nowStagePos = 0;
                break;
            case checking:
                nowStagePos = 1;
                break;
            case recruit:
                nowStagePos = 2;
                break;
            case doing:
                nowStagePos = 3;
                break;
            default: // finish:
                nowStagePos = 4;
                break;
        }

        final int pickTextColor = 0xFF6BC027;
        for (int i = 0; i < pointIds.length; ++i) {
            ImageView point = (ImageView) findViewById(pointIds[i]);
            if (i == nowStagePos) {
                point.setImageResource(R.mipmap.ic_subway_point_now);
                TextView textView = (TextView) findViewById(textIds[i]);
                textView.setTextColor(pickTextColor);
            } else if (i < nowStagePos) {
                point.setImageResource(R.mipmap.ic_subway_point_over);
            } else {
                point.setImageResource(R.mipmap.ic_subway_future);
            }
        }

        if (progress == Progress.doing) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup subwayLayout = (ViewGroup) inflater.inflate(R.layout.subway_layout, stageLayout, false);
            stageLayout.addView(subwayLayout);

            List<Role> roles = metro.roles;
            int rolePos = 0;
            for (Role role : roles) {
                ViewGroup layoutLine = (ViewGroup) inflater.inflate(R.layout.subway_layout_line, subwayLayout, false);
                subwayLayout.addView(layoutLine);

                List<Stage> stages = role.stages;
                int randomColor = getRoleColor(rolePos, roles.size());
                ++rolePos;
                for (Stage stage : stages) {
                    View lineItem = inflater.inflate(R.layout.subway_layout_line_item, layoutLine, false);

                    TextView textView = (TextView) lineItem.findViewById(R.id.text);
                    textView.setText(stage.stageNo);

                    GradientDrawable drawable = (GradientDrawable) lineItem.getBackground();
                    int color;
                    if (stage.getStatusEnum().hightLight()) {
                        color = randomColor;
                    } else {
                        color = 0xffD7D8D7;
                    }

                    drawable.setColor(color);

                    layoutLine.addView(lineItem);
                }
            }
        }

        post(() -> {
            if (findViewById(R.id.rootLayout).getWidth() >= LengthUtil.getsWidthPix()) {
                return;
            }

            int[] linesFull = new int[] {
                    R.id.line0,
                    R.id.line1,
                    R.id.line2,
                    R.id.line5
            };

            // line3 和 line4 都算半根
            int[] linesHalf = new int[] {
                    R.id.line3,
                    R.id.line4
            };

            resizeAllLines(linesFull, linesHalf);
        });
    }

    public static int getRoleColor(int pos, int count) {
        final int[] colors = new int[] {
                0xFFF28C08,
                0xFF68C20D,
                0xFF256DC3,
                0xFFCC46C3,
                0xFFDFDF17
        };

        if (count <= 1) {
            return colors[1];
        }

        return colors[pos % colors.length];
    }


}
