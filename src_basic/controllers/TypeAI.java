package controllers;

/**
 * Different behaviour for AI.
 * CLASSIC will send 30% of each planets available ships every 60 frames.
 * SAFE will send 10%.
 * AGGRESSIVE will send 50%.
 */
public enum TypeAI {
	CLASSIC,
	SAFE,
	AGGRESSIVE;
}
