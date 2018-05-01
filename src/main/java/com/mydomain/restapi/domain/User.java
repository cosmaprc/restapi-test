package com.mydomain.restapi.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mydomain.restapi.util.Constants;
import com.mydomain.restapi.util.I18nUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = Constants.CONSTRAINT_UQ_USER_USERNAME, columnNames = "username"))
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "users_generator", sequenceName = "users_sequence", allocationSize = 1)
	@GeneratedValue(generator = "users_generator")
	@Column(name = "id")
	@JsonIgnore
	private Long id;

	@Column(name = "username")
	@NotNull
	@Size(min = 1, max = 50, message = I18nUtil.I18nKeys.ERROR_USER_USERNAME_SIZE)
	@JsonProperty("username")
	private String userName;

	@Column(name = "firstname")
	@NotNull
	@Size(min = 1, max = 50, message = I18nUtil.I18nKeys.ERROR_USER_FIRSTNAME_SIZE)
	@JsonProperty("firstname")
	private String firstName;

	@Column(name = "lastname")
	@NotNull
	@Size(min = 1, max = 50, message = I18nUtil.I18nKeys.ERROR_USER_LASTNAME_SIZE)
	@JsonProperty("lastname")
	private String lastName;

	@Column(name = "age")
	@NotNull
	@Min(value = 0, message = I18nUtil.I18nKeys.ERROR_USER_AGE_POSITIVE)
	@JsonProperty("age")
	private Integer age;

}
