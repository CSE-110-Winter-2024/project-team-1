package edu.ucsd.cse110.successorator.util;

import android.content.Intent;
import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class DateChangeReceiverTest {

    @Mock
    private Context mockContext;
    @Mock
    private Intent mockIntent;
    @Mock
    private Runnable mockUpdateDate;

    private DateChangeReceiver dateChangeReceiver;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        dateChangeReceiver = new DateChangeReceiver(mockUpdateDate);
    }

    @Test
    public void testOnReceive_WithDateChangedAction_CallsUpdateDate() {
        // Arrange
        when(mockIntent.getAction()).thenReturn(Intent.ACTION_DATE_CHANGED);

        // Act
        dateChangeReceiver.onReceive(mockContext, mockIntent);

        // Assert
        verify(mockUpdateDate, times(1)).run();
    }

    @Test
    public void testOnReceive_WithNonDateChangedAction_DoesNotCallUpdateDate() {
        // Arrange
        when(mockIntent.getAction()).thenReturn("some_other_action");

        // Act
        dateChangeReceiver.onReceive(mockContext, mockIntent);

        // Assert
        verify(mockUpdateDate, never()).run();
    }
}
