package com.funcheap.funmapsf.commons.interfaces;

/**
 * Created by Jayson on 10/25/2017.
 *
 * Interface used to pass onBackClick actions from Activities to Fragments
 */

public interface OnBackClickCallback {
    /**
     * Action to take when the user pressed back on an activity
     * @return true if backClick was handled, false if not.
     */
    boolean onBackClick();
}
