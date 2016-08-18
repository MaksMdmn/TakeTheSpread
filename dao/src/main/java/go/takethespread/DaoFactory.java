package go.takethespread;

import go.takethespread.exceptions.PersistException;

import java.sql.SQLException;

public interface DaoFactory<Context> {

    public interface DaoCreator<Context>{
        public GenericDao create(Context context);
    }

    public Context getContext() throws PersistException;

    public GenericDao getDao(Context Context, Class dtoClass) throws PersistException;
}
