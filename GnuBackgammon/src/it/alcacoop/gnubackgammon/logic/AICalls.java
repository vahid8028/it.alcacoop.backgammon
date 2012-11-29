package it.alcacoop.gnubackgammon.logic;

import it.alcacoop.gnubackgammon.actors.Board;
import it.alcacoop.gnubackgammon.aicalls.*;
import it.alcacoop.gnubackgammon.logic.AILevels;

public class AICalls {
  
  static AIThread thread = new AIThread();
  
  static {
    thread = new AIThread();
  }

  public static void SetAILevel(AILevels l) {
    MatchState.currentLevel = l;
    thread.post(new SetAILevelAICall(l));
  }
  
  public static void RollDice() {
    thread.post(new RollDiceAICall());
  }
  
  public static void SetBoard(int b1[], int b2[]) {
    thread.post(new SetBoardAICall(b1, b2));
  }
  
  public static void AcceptResign(int r) {
    thread.post(new AcceptResignAICall(r));
  }

  public static void AcceptDouble() {
    thread.post(new AcceptDoubleAICall());
  }
  
  public static void UpdateMSCubeInfo(int c, int f) {
    thread.post(new UpdateMSCubeInfoAICall(c, f));
  }
  
  public static void AskForResignation() {
    thread.post(new AskForResignationAICall());
  }

  public static void AskForDoubling() {
    thread.post(new AskForDoublingAICall());
  }

  public static void EvaluateBestMove(int dices[]) {
    thread.post(new EvaluateBestMoveAICall(dices));
  }
  
  public static void SetMatchScore(int AIScore, int HumanScore) {
    thread.post(new SetMatchScoreAICall(AIScore, HumanScore));
  }
  
  public static void SetGameTurn(int fTurn, int fMove) {
    thread.post(new SetGameTurnAICall(fTurn, fMove));
  }
  
  public static void GenerateMoves(Board b, int d1, int d2) {
    thread.post(new GenerateMovesAICall(b, d1, d2));
  }
}