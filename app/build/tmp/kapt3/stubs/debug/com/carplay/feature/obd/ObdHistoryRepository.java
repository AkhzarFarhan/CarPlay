package com.carplay.feature.obd;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bJ\u001c\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\tH\u0086@\u00a2\u0006\u0002\u0010\u000fR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/carplay/feature/obd/ObdHistoryRepository;", "", "()V", "database", "Lcom/google/firebase/database/FirebaseDatabase;", "ref", "Lcom/google/firebase/database/DatabaseReference;", "getHistoryFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/carplay/feature/obd/ObdReport;", "uploadReport", "", "faultCodes", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ObdHistoryRepository {
    @org.jetbrains.annotations.Nullable()
    private final com.google.firebase.database.FirebaseDatabase database = null;
    @org.jetbrains.annotations.Nullable()
    private final com.google.firebase.database.DatabaseReference ref = null;
    
    public ObdHistoryRepository() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object uploadReport(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> faultCodes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.carplay.feature.obd.ObdReport>> getHistoryFlow() {
        return null;
    }
}