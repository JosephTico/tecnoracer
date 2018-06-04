package helpers;

import screens.GameStage;
import screens.MainMenu;

public enum ScreenEnum {

	MAIN_MENU {
		public AbstractScreen getScreen(Object... params) {
			return new MainMenu();
		}
	},
	GAME_STAGE {
		public AbstractScreen getScreen(Object... params) {
			return new GameStage();
		}
	};

	public abstract AbstractScreen getScreen(Object... params);
}