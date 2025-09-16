package threeoone.bigproject.controller.requestbodies;

import threeoone.bigproject.controller.SceneName;

/**
 * A request body for switching scenes.
 *
 * @param nameScene The name of the scene to switch to.
 */
public record SwitchScene(SceneName nameScene) {
}
