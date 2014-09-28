# Introduction

This is a rough stab at integrating [JOOQ](http://www.jooq.org/) into [Dropwizard](http://dropwizard.io/), as an alternative to the recommended JDBI solution

# Justification

JDBI works okay for simple queries like ````select id from person````, but quickly becomes cumbersome when you want to create more complex queries with dynamic ````order by```` and ````where```` clauses. 

Also the documentation for JDBI is thin on the ground and hard to dig through.

# Status

This is very early code and has no unit tests right now. I would not recommend this for production code unless you really know what you are doing

# Usage

In your application's ````run```` method (remember to change ````SQLDialect```` to your DBMS of choice if not using Postgres)

	@Override
	public void run(ExampleConfiguration config,
					Environment environment) throws ClassNotFoundException {
        final JooqDSLContextFactory dslContextFactory = new JooqDSLContextFactory();
        this.dslContext = dslContextFactory.build(environment, configuration.getDataSourceFactory(), SQLDialect.POSTGRES);
		final UserDAO dao =  new UserDAO(dslContext);
		// other application code...
	}

A DAO can be something like this

	public class UserDAO {
	  private final DSLContext dslContext;
	  
	  public UserDAO(final DSLContext dslContext) {
		this.dslContext = dslContext;
	  }

	  public String findNameById(int id) {
		return this.dslContext.select(field("id")).from("users").fetch();
	  }
	}
# Outstanding tasks

* See [Issues]()

