package com.eiilo.mood.dude.handlers;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.eiilo.mood.dude.MainMenu;
import com.eiilo.mood.dude.env.MD;
import com.eiilo.mood.dude.utils.LevelSelector;

public class LevelSelectorGestureHandler implements GestureDetector.GestureListener {

    private Screen screen;
    private int currentPage;

    public LevelSelectorGestureHandler (Screen sc, int pageIndex) {
        this.currentPage = pageIndex;
        this.screen = screen;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 v = new Vector3(x, y, 0);
        LevelSelector.mainCamera.unproject(v);
        if (LevelSelector.btnSprites.get(0).getBoundingRectangle().contains(v.x, v.y)) {
            LevelSelector.game.setScreen(new MainMenu(LevelSelector.game));
            return true;
        }
        if (LevelSelector.indexSprites.get(0).getBoundingRectangle().contains(v.x, v.y) && currentPage != 1) {
            LevelSelector.game.setScreen(new LevelSelector(LevelSelector.game,1));
            return true;
        }
        else if (LevelSelector.indexSprites.get(1).getBoundingRectangle().contains(v.x, v.y) && currentPage != 2) {
            LevelSelector.game.setScreen(new LevelSelector(LevelSelector.game,2));
            return true;
        }
        else if (LevelSelector.indexSprites.get(2).getBoundingRectangle().contains(v.x, v.y) && currentPage != 3) {
            LevelSelector.game.setScreen(new LevelSelector(LevelSelector.game,3));
            return true;
        }
        if (currentPage == 1) {
            if (LevelSelector.levelsSprites.get(0).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 1) {
                LevelSelector.game.setScreen(new LevelLoader(1, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(1).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 2) {
                LevelSelector.game.setScreen(new LevelLoader(2, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(2).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 3) {
                LevelSelector.game.setScreen(new LevelLoader(3, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(3).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 4) {
                LevelSelector.game.setScreen(new LevelLoader(4, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(4).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 5) {
                LevelSelector.game.setScreen(new LevelLoader(5, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(5).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 6) {
                LevelSelector.game.setScreen(new LevelLoader(6, LevelSelector.game));
            }
            return true;
        }
        else if (currentPage == 2) {
            if (LevelSelector.levelsSprites.get(0).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 7) {
                LevelSelector.game.setScreen(new LevelLoader(7, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(1).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 8) {
                LevelSelector.game.setScreen(new LevelLoader(8, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(2).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 9) {
                LevelSelector.game.setScreen(new LevelLoader(9, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(3).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 10) {
                LevelSelector.game.setScreen(new LevelLoader(10, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(4).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 11) {
                LevelSelector.game.setScreen(new LevelLoader(11, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(5).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 12) {
                LevelSelector.game.setScreen(new LevelLoader(12, LevelSelector.game));
            }
            return true;
        }
        else if (currentPage == 3) {
            if (LevelSelector.levelsSprites.get(0).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 13) {
                LevelSelector.game.setScreen(new LevelLoader(13, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(1).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 14) {
                LevelSelector.game.setScreen(new LevelLoader(14, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(2).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 15) {
                LevelSelector.game.setScreen(new LevelLoader(15, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(3).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 16) {
                LevelSelector.game.setScreen(new LevelLoader(16, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(4).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 17) {
                LevelSelector.game.setScreen(new LevelLoader(17, LevelSelector.game));
            }
            else if (LevelSelector.levelsSprites.get(5).getBoundingRectangle().contains(v.x, v.y) && MD.lastLevel >= 18) {
                LevelSelector.game.setScreen(new LevelLoader(18, LevelSelector.game));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (velocityX > 3) {
            if (currentPage == 1)
                return false;
            else if (currentPage == 2)
                LevelSelector.game.setScreen(new LevelSelector(LevelSelector.game,1));
            else if (currentPage == 3)
                LevelSelector.game.setScreen(new LevelSelector(LevelSelector.game,2));
        }
        if (velocityX < -3) {
            if (currentPage == 1)
                LevelSelector.game.setScreen(new LevelSelector(LevelSelector.game,2));
            else if (currentPage == 2)
                LevelSelector.game.setScreen(new LevelSelector(LevelSelector.game,3));
            else if (currentPage == 3)
                return false;
        }
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
