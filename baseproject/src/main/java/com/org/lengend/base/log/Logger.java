/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.org.lengend.base.log;

import android.util.Log;

/**
 * Helper class to redirect {@link } to {@link Log}
 */
public final class Logger{
//    private static final boolean DEBUG = Log.isLoggable(Logger.class.getSimpleName(), Log.DEBUG);
    private static final boolean DEBUG = true;


    private Logger(){
        throw new RuntimeException(this.getClass().getSimpleName() + " should not be instantiated");
    }

    public static int v(String tag, String msg) {

        if(DEBUG){
            return Log.v(tag, msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if(DEBUG){
            return Log.v(tag, msg, tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if(DEBUG){
            return Log.d(tag, msg);
        }
        return 0;
    }


    public static int d(String tag, String msg, Throwable tr) {
        if(DEBUG){
            return Log.d(tag, msg, tr);
        }
        return 0;

    }


    public static int i(String tag, String msg) {
        if(DEBUG){
            return Log.i(tag, msg);
        }
        return 0;
    }


    public static int i(String tag, String msg, Throwable tr) {
        if(DEBUG){
            return Log.i(tag, msg, tr);
        }
        return 0;
    }


    public static int w(String tag, String msg) {
        if(DEBUG){
            return Log.w(tag, msg);
        }
        return 0;
    }


    public int w(String tag, String msg, Throwable tr) {
        if(DEBUG){
            return Log.w(tag, msg, tr);
        }
        return 0;
    }


    public static int e(String tag, String msg) {
        if(DEBUG){
            return Log.e(tag, msg);
        }
        return 0;
    }


    public static int e(String tag, String msg, Throwable tr) {
        if(DEBUG){
            return Log.e(tag, msg, tr);
        }
        return 0;
    }
}
