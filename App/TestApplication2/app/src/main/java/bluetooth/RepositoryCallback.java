package bluetooth;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
