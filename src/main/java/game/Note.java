package game;

public class Note {
    private final Lane lane;
    private final long hitTimeMs;
    private NoteState state = NoteState.PENDING;

    public Note(Lane lane, long hitTimeMs) {
        this.lane = lane;
        this.hitTimeMs = hitTimeMs;
    }

    public Lane getLane() {
        return lane;
    }

    public long getHitTimeMs() {
        return hitTimeMs;
    }

    public NoteState getState() {
        return state;
    }

    public boolean isPending() {
        return state == NoteState.PENDING;
    }

    public void markHit() {
        state = NoteState.HIT;
    }

    public void markMissed() {
        state = NoteState.MISSED;
    }
}
