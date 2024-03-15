package edu.ucsd.cse110.successorator.lib.domain;

public enum TaskContextMenuOption {
    MoveToToday("Move to Today"),
    MoveToTomorrow("Move to Tomorrow"),
    Finish("Finish"),
    Delete("Delete");

    private final String title;

    TaskContextMenuOption(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}