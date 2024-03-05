package com.wny.wnyflix;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WnyflixApplicationTests {

	@Test
	public String asdasd() {

		class User {
			String id;
			String password;
			String name;
			int age;

			public User(String id, String password, String name) {
				this.id = id;
				this.password = password;
				this.name = name;
			}

			public User(String id, String password, String name, int age) {
				this.id = id;
				this.password = password;
				this.name = name;
				this.age = age;
			}

			public User(User user, int age) {

			}

			@Override
			public String toString() {
				return "User{" +
						"id='" + id + '\'' +
						", password='" + password + '\'' +
						", name='" + name + '\'' +
						", age=" + age +
						'}';
			}
		}

		class Idpw {
			String id;
			String password;
			String name;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}

		Idpw idpw = new Idpw();

		idpw.setId("id");
		idpw.setPassword("pwd");
		idpw.setName("name");
		User user = new User(idpw.getId(), idpw.getPassword(), idpw.getName());

		User user2 = new User(user, 1);

		return user2.toString();

	}

}
