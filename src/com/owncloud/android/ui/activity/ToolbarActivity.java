/**
 *   Nextcloud Android client application
 *
 *   @author Andy Scherzinger
 *   Copyright (C) 2016 Andy Scherzinger
 *   Copyright (C) 2016 Nextcloud
 *   Copyright (C) 2016 ownCloud Inc.
 *
 *   This program is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU AFFERO GENERAL PUBLIC LICENSE
 *   License as published by the Free Software Foundation; either
 *   version 3 of the License, or any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU AFFERO GENERAL PUBLIC LICENSE for more details.
 *
 *   You should have received a copy of the GNU Affero General Public
 *   License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.owncloud.android.ui.activity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.owncloud.android.R;
import com.owncloud.android.datamodel.FileDataStorageManager;
import com.owncloud.android.datamodel.OCFile;

import org.jetbrains.annotations.NotNull;

import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabStripBuild;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

/**
 * Base class providing toolbar registration functionality, see {@link #setupToolbar()}.
 */
public abstract class ToolbarActivity extends BaseActivity {
    private ProgressBar mProgressBar;

    // Bottom tab bar controller
    Controller mTabController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void setupTabbar(TabItemData[] tabItemDatas, @ColorInt int backgroundColor,OnTabItemSelectListener listener, int defaultTab) {
        if(tabItemDatas == null || tabItemDatas.length <= 0) {
            return;
        }

        try {
            PagerBottomTabLayout pagerBottomTabLayout = (PagerBottomTabLayout) findViewById(R.id.tabBar);
            if (pagerBottomTabLayout == null) {
                return;
            }
            //构建导航栏,得到Controller进行后续控制
            TabStripBuild tabBuilder = pagerBottomTabLayout.builder();
            for (TabItemData item :
                    tabItemDatas) {
                //用TabItemBuilder构建一个导航按钮
                tabBuilder.addTabItem(item.drawable,item.selectedDrawable,item.text,item.selectedColor);
            }
            mTabController = tabBuilder.build();
            if(backgroundColor > 0) {
                mTabController.setBackgroundColor(backgroundColor);
            }
            if(listener != null) {
                mTabController.addTabItemClickListener(listener);
            }

            if(defaultTab >= 0 && defaultTab < tabItemDatas.length) {
                mTabController.setSelect(defaultTab);
            }
        }
        catch(Exception e) {
            return;
        }

//        controller.setMessageNumber("A",2);
//        controller.setDisplayOval(0,true);
    }


    /**
     * Toolbar setup that must be called in implementer's {@link #onCreate} after {@link #setContentView} if they
     * want to use the toolbar.
     */
    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setIndeterminateDrawable(
                ContextCompat.getDrawable(this, R.drawable.actionbar_progress_indeterminate_horizontal));
    }

    /**
     * Updates title bar and home buttons (state and icon).
     */
    protected void updateActionBarTitleAndHomeButton(OCFile chosenFile) {
        String title = getString(R.string.default_display_name_for_root_folder);    // default
        boolean inRoot;

        // choose the appropriate title
        inRoot = (
                chosenFile == null ||
                        (chosenFile.isFolder() && chosenFile.getParentId() == FileDataStorageManager.ROOT_PARENT_ID)
        );
        if (!inRoot) {
            title = chosenFile.getFileName();
        }

        updateActionBarTitleAndHomeButtonByString(title);
    }

    /**
     * Updates title bar and home buttons (state and icon).
     */
    protected void updateActionBarTitleAndHomeButtonByString(String title) {
        String titleToSet = getString(R.string.app_name);    // default

        if (title != null) {
            titleToSet = title;
        }

        // set the chosen title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(titleToSet);

        // set home button properties
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    /**
     * checks if the given file is the root folder.
     *
     * @param file file to be checked if it is the root folder
     * @return <code>true</code> if it is <code>null</code> or the root folder, else returns <code>false</code>
     */
    public boolean isRoot(OCFile file) {
        return file == null ||
                (file.isFolder() && file.getParentId() == FileDataStorageManager.ROOT_PARENT_ID);
    }

    /**
     * Change the indeterminate mode for the toolbar's progress bar.
     *
     * @param indeterminate <code>true</code> to enable the indeterminate mode
     */
    public void setIndeterminate(boolean indeterminate) {
        mProgressBar.setIndeterminate(indeterminate);
    }

    /**
     * Set the background to to progress bar of the toolbar. The resource should refer to
     * a Drawable object or 0 to remove the background.#
     *
     * @param color The identifier of the color.
     * @attr ref android.R.styleable#View_background
     */
    public void setProgressBarBackgroundColor(@ColorInt int color) {
        mProgressBar.setBackgroundColor(color);
        mProgressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}

class TabItemData {
    public TabItemData(@DrawableRes int drawable, @DrawableRes int selectedDrawable, @NotNull String text, @ColorInt int selectedColor) {
        this.drawable = drawable;
        this.selectedDrawable = selectedDrawable;
        this.text = text;
        this.selectedColor = selectedColor;
    }

    int drawable;
    int selectedDrawable;
    String text;
    int selectedColor;
}