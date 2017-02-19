/**
 * Nextcloud Android client application
 *
 * @author Andy Scherzinger
 * @author Tobias Kaminsky
 * Copyright (C) 2016 Andy Scherzinger
 * Copyright (C) 2016 Nextcloud
 * <p/>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU AFFERO GENERAL PUBLIC LICENSE
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU AFFERO GENERAL PUBLIC LICENSE for more details.
 * <p/>
 * You should have received a copy of the GNU Affero General Public
 * License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.owncloud.android.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.owncloud.android.R;
import com.owncloud.android.lib.common.utils.Log_OC;

import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

/**
 * Activity providing information about ways to participate in the app's development.
 */
public class ParticipateActivity extends FileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.participate_layout);

        // setup toolbar
        setupToolbar();

        // setup drawer
        setupDrawer(R.id.nav_participate);
        getSupportActionBar().setTitle(getString(R.string.drawer_participate));

        setupContent();

        // Setup bottom tab bar
        TabItemData[] tabItems = new TabItemData[4];
        tabItems[0] = new TabItemData(android.R.drawable.ic_menu_send,android.R.drawable.ic_menu_send,"智云", getResources().getColor(R.color.color_accent));
        tabItems[1] = new TabItemData(android.R.drawable.ic_menu_compass,android.R.drawable.ic_menu_compass,"简记",getResources().getColor(R.color.color_accent));
        tabItems[2] = new TabItemData(android.R.drawable.ic_menu_search,android.R.drawable.ic_menu_search,"搜索",getResources().getColor(R.color.color_accent));
        tabItems[3] = new TabItemData(android.R.drawable.ic_menu_help,android.R.drawable.ic_menu_help,"帮助",getResources().getColor(R.color.color_accent));
        setupTabbar(tabItems,getResources().getColor(R.color.background_color),mTabListener,3);

    }

    OnTabItemSelectListener mTabListener = new OnTabItemSelectListener() {
        @Override
        public void onSelected(int index, Object tag)
        {
            Log_OC.v(TAG, "onSelected:"+index+"   TAG: "+tag.toString());
            switch (index) {
                case 0:
                    showFiles(false);
                    break;
                case 1:
                    Intent uploadListIntent = new Intent(getApplicationContext(),
                            UploadListActivity.class);
                    uploadListIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(uploadListIntent);
                    break;
            }
        }

        @Override
        public void onRepeatClick(int index, Object tag) {
            Log_OC.v(TAG, "onRepeatClick:"+index+"   TAG: "+tag.toString());
        }
    };

    private void setupContent() {
        TextView betaView = (TextView) findViewById(R.id.participate_beta_text);
        betaView.setMovementMethod(LinkMovementMethod.getInstance());
        betaView.setText(Html.fromHtml(getString(R.string.participate_beta_text,
                getString(R.string.fdroid_beta_link),
                getString(R.string.beta_apk_link))));


        TextView rcView = (TextView) findViewById(R.id.participate_release_candidate_text);
        rcView.setMovementMethod(LinkMovementMethod.getInstance());

        TextView contributeIrcView = (TextView) findViewById(R.id.participate_contribute_irc_text);
        contributeIrcView.setMovementMethod(LinkMovementMethod.getInstance());
        contributeIrcView.setText(Html.fromHtml(
                getString(R.string.participate_contribute_irc_text,
                        getString(R.string.irc_weblink)
                )));

        TextView contributeForumView = (TextView) findViewById(R.id.participate_contribute_forum_text);
        contributeForumView.setMovementMethod(LinkMovementMethod.getInstance());
        contributeForumView.setText(Html.fromHtml(
                getString(R.string.participate_contribute_forum_text,
                        getString(R.string.help_link)
                )));

        TextView contributeTranslationView = (TextView) findViewById(R.id.participate_contribute_translate_text);
        contributeTranslationView.setMovementMethod(LinkMovementMethod.getInstance());
        contributeTranslationView.setText(Html.fromHtml(
                getString(R.string.participate_contribute_translate_text,
                        getString(R.string.translation_link)
                )));

        TextView contributeGithubView = (TextView) findViewById(R.id.participate_contribute_github_text);
        contributeGithubView.setMovementMethod(LinkMovementMethod.getInstance());
        contributeGithubView.setText(Html.fromHtml(getString(R.string.participate_contribute_github_text)));

        findViewById(R.id.participate_testing_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.report_issue_link))));
            }
        });
    }

    public void onGetBetaFDroidClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.fdroid_beta_link))));
    }

    public void onGetRCFDroidClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.fdroid_link))));
    }

    public void onGetRCPlayStoreClick(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.play_store_register_beta))));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean retval;
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (isDrawerOpen()) {
                    closeDrawer();
                } else {
                    openDrawer();
                }
            }

            default:
                retval = super.onOptionsItemSelected(item);
        }
        return retval;
    }

    @Override
    public void showFiles(boolean onDeviceOnly) {
        super.showFiles(onDeviceOnly);
        Intent fileDisplayActivity = new Intent(getApplicationContext(),
                FileDisplayActivity.class);
        fileDisplayActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(fileDisplayActivity);
    }
}
