package game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameplaySessionTest {
    @Test
    void testHitsNearestPendingNoteInRequestedLaneOnlyOnce() {
        Note first = new Note(Lane.D, 1_000);
        Note second = new Note(Lane.D, 1_100);
        Note otherLane = new Note(Lane.F, 1_050);
        GameplaySession session = session(first, second, otherLane);

        assertEquals(HitJudgement.PERFECT_300, session.hit(Lane.D, 1_090));
        assertEquals(NoteState.PENDING, first.getState());
        assertEquals(NoteState.HIT, second.getState());
        assertEquals(HitJudgement.GREAT_200, session.hit(Lane.D, 1_090));
        assertEquals(NoteState.HIT, first.getState());
        assertEquals(NoteState.PENDING, otherLane.getState());
    }

    @Test
    void testMissesExpiredNotesAndResetsCombo() {
        Note hitNote = new Note(Lane.D, 1_000);
        Note missedNote = new Note(Lane.F, 2_000);
        GameplaySession session = session(hitNote, missedNote);

        session.hit(Lane.D, 1_000);
        assertEquals(1, session.getScoreTracker().getCombo());

        session.update(2_000 + HitJudgement.MAX_HIT_WINDOW_MS + 1);

        assertEquals(NoteState.MISSED, missedNote.getState());
        assertEquals(0, session.getScoreTracker().getCombo());
        assertEquals(1, session.getScoreTracker().getMaximumCombo());
        assertTrue(session.isComplete());
    }

    @Test
    void testIgnoresKeysWithNoNoteInsideTheHitWindow() {
        GameplaySession session = session(new Note(Lane.K, 1_000));

        assertNull(session.hit(Lane.K, 500));
        assertEquals(0, session.getScoreTracker().getScore());
    }

    private GameplaySession session(Note... notes) { // the ... is a varargs parameter, allowing multiple Note objects to be passed
        Beatmap beatmap = new Beatmap("song", "artist", Difficulty.EASY, 120, 1_500, List.of(notes));
        return new GameplaySession(beatmap, new GameConfig(Difficulty.EASY, "Test player"));
    }
}
