/*
 * DVRecentRecordProcess.java  2/6/13 1:04 PM
 *
 * Copyright (C) 2012-2013 Nick Ma
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dv.ui.frequent;

import com.dv.prop.DVPropMain;
import com.dv.util.DateFormatFactory;

import java.util.*;

/**
 * @author xyma
 */
public class DVRecentRecordProcess {

    public static boolean hasRecentRecord(String fname) {

        try {
            if (DVPropMain.DV_RECENT_INSTANCE_COUNT.get(fname) == null) {

                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void setRecentRecordInfo(String name) {

        String serverName = name;

        if (hasRecentRecord(serverName)) {
            int newCount = Integer.valueOf(DVPropMain.DV_RECENT_INSTANCE_COUNT.get(serverName)) + 1;
            DVPropMain.DV_RECENT_INSTANCE_COUNT.put(serverName, String.valueOf(newCount));
            DVPropMain.DV_RECENT_INSTANCE_LASTTIME.put(serverName, DateFormatFactory.getCurrentDateTimeString());
        } else {
            DVPropMain.DV_RECENT_INSTANCE_SEQ.put(serverName, String.valueOf(DVPropMain.DV_RECENT_INSTANCE_SEQ.size() + 1));
            DVPropMain.DV_RECENT_INSTANCE_NAME.put(String.valueOf(DVPropMain.DV_RECENT_INSTANCE_NAME.size() + 1), serverName);
            DVPropMain.DV_RECENT_INSTANCE_COUNT.put(serverName, String.valueOf(1));
            DVPropMain.DV_RECENT_INSTANCE_LASTTIME.put(serverName, DateFormatFactory.getCurrentDateTimeString());
            DVPropMain.DV_RECENT_INSTANCE_SQLFILEFULLPATH.put(serverName, "");
            DVPropMain.DV_RECENT_INSTANCE_LAST_EXECUTE_SQL_LINE.put(serverName, "-99");
        }
        buildNewSEQ(serverName);
    }


    public static void removeRecentServerRecord(String serverName) {

        if (hasRecentRecord(serverName)) {
            String index = DVPropMain.DV_RECENT_INSTANCE_SEQ.get(serverName);
            DVPropMain.DV_RECENT_INSTANCE_SEQ.remove(serverName);
            DVPropMain.DV_RECENT_INSTANCE_NAME.remove(index);
            DVPropMain.DV_RECENT_INSTANCE_COUNT.remove(serverName);
            DVPropMain.DV_RECENT_INSTANCE_LASTTIME.remove(serverName);
            DVPropMain.DV_RECENT_INSTANCE_SQLFILEFULLPATH.remove(serverName);
            DVPropMain.DV_RECENT_INSTANCE_LAST_EXECUTE_SQL_LINE.remove(serverName);
            buildRemoveSeq();
        }
    }

    private static void buildRemoveSeq() {

        int count = DVPropMain.DV_RECENT_INSTANCE_COUNT.size();

        if (count > 1) {

            ArrayList keys = new ArrayList(DVPropMain.DV_RECENT_INSTANCE_COUNT.keySet());

            Collections.sort(keys, new DVComparator(DVPropMain.DV_RECENT_INSTANCE_COUNT));

            DVPropMain.DV_RECENT_INSTANCE_SEQ.clear();
            DVPropMain.DV_RECENT_INSTANCE_NAME.clear();
            for (int i = 1; i <= keys.size(); i++) {

                DVPropMain.DV_RECENT_INSTANCE_SEQ.put(keys.get(i - 1).toString(), String.valueOf(i));
                DVPropMain.DV_RECENT_INSTANCE_NAME.put(String.valueOf(i).trim(), keys.get(i - 1).toString());
            }
        } else if (count == 1) {

            ArrayList keys = new ArrayList(DVPropMain.DV_RECENT_INSTANCE_COUNT.keySet());
            String serverName = DVPropMain.DV_RECENT_INSTANCE_SEQ.get(keys.get(0).toString().trim());
            DVPropMain.DV_RECENT_INSTANCE_SEQ.remove(keys.get(0).toString().trim());
            DVPropMain.DV_RECENT_INSTANCE_SEQ.put(String.valueOf(1), serverName);
            DVPropMain.DV_RECENT_INSTANCE_NAME.put(serverName, String.valueOf(1));
        }

    }

    private static void buildNewSEQ(String fname) {

        int count = DVPropMain.DV_RECENT_INSTANCE_COUNT.size();

        if (count > 1) {

            ArrayList keys = new ArrayList(DVPropMain.DV_RECENT_INSTANCE_COUNT.keySet());

            Collections.sort(keys, new DVComparator(DVPropMain.DV_RECENT_INSTANCE_COUNT));

            DVPropMain.DV_RECENT_INSTANCE_SEQ.clear();
            DVPropMain.DV_RECENT_INSTANCE_NAME.clear();
            for (int i = 1; i <= keys.size(); i++) {

                DVPropMain.DV_RECENT_INSTANCE_SEQ.put(keys.get(i - 1).toString(), String.valueOf(i));
                DVPropMain.DV_RECENT_INSTANCE_NAME.put(String.valueOf(i).trim(), keys.get(i - 1).toString());
            }
        } else {

            DVPropMain.DV_RECENT_INSTANCE_SEQ.put(fname, "1");
            DVPropMain.DV_RECENT_INSTANCE_NAME.put("1", fname);

        }
    }

}
