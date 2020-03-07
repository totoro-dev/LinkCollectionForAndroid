package top.totoro.linkcollection.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import top.totoro.linkcollection.android.R;
import top.totoro.linkcollection.android.util.FindView;
import top.totoro.linkcollection.android.util.Logger;
import user.Info;

/**
 * Create by HLM on 2020-03-05
 */
public class ChooseLovesFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String art = "art";
    private final String science = "science";
    private final String computer = "computer";
    private final String healthy = "healthy";
    private final String economics = "economics";
    private final String life = "life";
    private final String game = "game";
    private final String eat = "eat";
    private final String tour = "tour";
    private RadioButton artRadio;
    private RadioButton scienceRadio;
    private RadioButton computerRadio;
    private RadioButton healthyRadio;
    private RadioButton economicRadio;
    private RadioButton lifeRadio;
    private RadioButton gameRadio;
    private RadioButton eatRadio;
    private RadioButton tourRadio;
    private LinearLayout artLayout;
    private LinearLayout scienceLayout;
    private LinearLayout computerLayout;
    private LinearLayout healthyLayout;
    private LinearLayout economicsLayout;
    private LinearLayout lifeLayout;
    private LinearLayout gameLayout;
    private LinearLayout eatLayout;
    private LinearLayout tourLayout;
    private FindView find;
    private List<String> loveList = new LinkedList<>();

    @Override
    public void onStart() {
        super.onStart();
        artRadio = find.RadioButton(R.id.radio_art);
        scienceRadio = find.RadioButton(R.id.radio_science);
        computerRadio = find.RadioButton(R.id.radio_computer);
        healthyRadio = find.RadioButton(R.id.radio_healthy);
        economicRadio = find.RadioButton(R.id.radio_economic);
        lifeRadio = find.RadioButton(R.id.radio_life);
        gameRadio = find.RadioButton(R.id.radio_game);
        eatRadio = find.RadioButton(R.id.radio_eat);
        tourRadio = find.RadioButton(R.id.radio_tour);
        artLayout = find.LinearLayout(R.id.art_ll);
        scienceLayout = find.LinearLayout(R.id.science_ll);
        computerLayout = find.LinearLayout(R.id.computer_ll);
        healthyLayout = find.LinearLayout(R.id.healthy_ll);
        economicsLayout = find.LinearLayout(R.id.economics_ll);
        lifeLayout = find.LinearLayout(R.id.life_ll);
        gameLayout = find.LinearLayout(R.id.game_ll);
        eatLayout = find.LinearLayout(R.id.eat_ll);
        tourLayout = find.LinearLayout(R.id.tour_ll);
        artRadio.setOnCheckedChangeListener(this);
        scienceRadio.setOnCheckedChangeListener(this);
        computerRadio.setOnCheckedChangeListener(this);
        healthyRadio.setOnCheckedChangeListener(this);
        economicRadio.setOnCheckedChangeListener(this);
        lifeRadio.setOnCheckedChangeListener(this);
        gameRadio.setOnCheckedChangeListener(this);
        eatRadio.setOnCheckedChangeListener(this);
        tourRadio.setOnCheckedChangeListener(this);
        artLayout.setOnClickListener(this);
        scienceLayout.setOnClickListener(this);
        computerLayout.setOnClickListener(this);
        healthyLayout.setOnClickListener(this);
        economicsLayout.setOnClickListener(this);
        lifeLayout.setOnClickListener(this);
        gameLayout.setOnClickListener(this);
        eatLayout.setOnClickListener(this);
        tourLayout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loveList.clear();
        String loveInfo = Info.getLoveInfo();
        if (loveInfo != null && loveInfo.length() > 0) {
            String[] loves = loveInfo.split(",");
            for (String love :
                    loves) {
                switch (love) {
                    case art:
                        loveList.add(art);
                        artRadio.setChecked(true);
                        break;
                    case science:
                        loveList.add(science);
                        scienceRadio.setChecked(true);
                        break;
                    case computer:
                        loveList.add(computer);
                        computerRadio.setChecked(true);
                        break;
                    case healthy:
                        loveList.add(healthy);
                        healthyRadio.setChecked(true);
                        break;
                    case economics:
                        loveList.add(economics);
                        economicRadio.setChecked(true);
                        break;
                    case life:
                        loveList.add(life);
                        lifeRadio.setChecked(true);
                        break;
                    case game:
                        loveList.add(game);
                        gameRadio.setChecked(true);
                        break;
                    case eat:
                        loveList.add(eat);
                        eatRadio.setChecked(true);
                        break;
                    case tour:
                        loveList.add(tour);
                        tourRadio.setChecked(true);
                        break;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_loves, container, false);
        find = new FindView(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.art_ll:
                changeRadioChecked(artRadio);
                break;
            case R.id.science_ll:
                changeRadioChecked(scienceRadio);
                break;
            case R.id.computer_ll:
                changeRadioChecked(computerRadio);
                break;
            case R.id.healthy_ll:
                changeRadioChecked(healthyRadio);
                break;
            case R.id.economics_ll:
                changeRadioChecked(economicRadio);
                break;
            case R.id.life_ll:
                changeRadioChecked(lifeRadio);
                break;
            case R.id.game_ll:
                changeRadioChecked(gameRadio);
                break;
            case R.id.eat_ll:
                changeRadioChecked(eatRadio);
                break;
            case R.id.tour_ll:
                changeRadioChecked(tourRadio);
                break;
        }
    }

    /**
     * 当单选框对应的Layout被点击时，改变单选框的选中状态
     *
     * @param loveRadio 被点击Layout里的单选框
     */
    private void changeRadioChecked(RadioButton loveRadio) {
        if (loveRadio.isChecked()) {
            loveRadio.setChecked(false);
        } else {
            loveRadio.setChecked(true);
        }
    }

    /**
     * 当单选框选中状态改变时，重置关注的list
     *
     * @param isChecked 是否选中
     * @param love      单选框对应的领域
     */
    private void checkLoveAtRadioCheckedChanged(boolean isChecked, String love) {
        if (isChecked) {
            for (String l : loveList) {
                if (love.equals(l)) return;
            }
            loveList.add(love);
        } else {
            loveList.remove(love);
            int size = loveList.size();
            int i = 0;
            while (i < size) {
                String l = loveList.get(i);
                if (love.equals(l)) {
                    loveList.remove(l);
                    size = loveList.size();
                    i--;
                }
                i++;
            }
        }
        Logger.d(this, "loves info = " + loveList.stream().distinct().collect(Collectors.joining(",")));
        Info.refreshLoves(loveList.stream().distinct().collect(Collectors.joining(",")));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.radio_art:
                checkLoveAtRadioCheckedChanged(isChecked, art);
                break;
            case R.id.radio_science:
                checkLoveAtRadioCheckedChanged(isChecked, science);
                break;
            case R.id.radio_computer:
                checkLoveAtRadioCheckedChanged(isChecked, computer);
                break;
            case R.id.radio_healthy:
                checkLoveAtRadioCheckedChanged(isChecked, healthy);
                break;
            case R.id.radio_economic:
                checkLoveAtRadioCheckedChanged(isChecked, economics);
                break;
            case R.id.radio_life:
                checkLoveAtRadioCheckedChanged(isChecked, life);
                break;
            case R.id.radio_game:
                checkLoveAtRadioCheckedChanged(isChecked, game);
                break;
            case R.id.radio_eat:
                checkLoveAtRadioCheckedChanged(isChecked, eat);
                break;
            case R.id.radio_tour:
                checkLoveAtRadioCheckedChanged(isChecked, tour);
                break;
        }
    }
}
