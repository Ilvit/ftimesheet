package com.timesheet;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.entities.AppUser;
import com.timesheet.entities.Role;
import com.timesheet.entities.Sheetday;
import com.timesheet.entities.TimesheetSaver;
import com.timesheet.entities.USAIDProject;
import com.timesheet.enums.Positions;
import com.timesheet.enums.RolesNames;
import com.timesheet.repositories.RoleRepository;
import com.timesheet.repositories.TimesheetSaverRepository;
import com.timesheet.repositories.USAIDProjectRepository;
import com.timesheet.services.AppUserService;
import com.timesheet.services.EmployeeService;
import com.timesheet.services.HolidayService;
import com.timesheet.services.SheetdayService;

@SpringBootApplication
public class TimesheetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimesheetServiceApplication.class, args);
	}	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean 
	CommandLineRunner clr(EmployeeService employeeService, SheetdayService sheetdayService, RoleRepository appRoleRepository,USAIDProjectRepository projectRepository,
			TimesheetSaverRepository savedSheetLineRepository, AppUserService appUserService, HolidayService holidayService) {
		return  args->{
			//Holidays
			/*
			 * holidayService.addHoliday(LocalDate.parse("lundi_18-mars-2024",
			 * TimesheetPeriods.dtf), "Faradja's USAID-ULINZI day");
			 * holidayService.addHoliday(LocalDate.parse("dimanche_30-juin-2024",
			 * TimesheetPeriods.dtf), "DRC independence day");
			 * holidayService.addHoliday(LocalDate.parse("jeudi_01-août-2024",
			 * TimesheetPeriods.dtf), "DRC day of the dead");
			 
			
			*/
			//Projects
			projectRepository.save(new USAIDProject(null,"USAID ULINZI","Key Population project"));
			projectRepository.save(new USAIDProject(null,"EPIC","Epidemiologic Control"));
			
			//Add employees
			employeeService.addNewEmployee("SEYA", "SENEFU", "Francine","MAFA005UU", "fseya@maisonfaradja.org", Positions.RECEPTIONIST);
			employeeService.addNewEmployee("KYUNGU", "MWANANGOY", "Tresor","MAFA013UU", "tresorkyungu@maisonfaradja.org", Positions.SITE_SUPERVISOR_KASAJI);
			employeeService.addNewEmployee("MUCHECHE", "NYANDWE", "Jean-Paul","MAFA011UU", "jpmucheche@maisonfaradja.org", Positions.SITE_SUPERVISOR_KAPANGA);
			employeeService.addNewEmployee("NGOIE", "MUTOMBO", "Didier","MAFA001UU", "didierngoie@maisonfaradja.org", Positions.COP);
			employeeService.addNewEmployee("NGOY", "KABWE", "Delly","MAFA002UU", "dellyngoy@maisonfaradja.org", Positions.DCOP);
//			employeeService.addNewEmployee("KASHIKA", "KASHIKA", "Clement","0006", "clementkashika@maisonfaradja.org", Positions.FINANCE_ASSOCIATE);
//			employeeService.addNewEmployee("BOMASI", "BOMASI", "Michel","0007", "michelbomasi@maisonfaradja.org", Positions.FINANCE_OFFICER);
//			employeeService.addNewEmployee("MUKUMBA", "MUKUMBA", "Curtis","0008", "curtismukumba@maisonfaradja.org", Positions.SSI);
			employeeService.addNewEmployee("KALWA", "TUNDWA", "Patty", "MAFA009UU","pattykalwa@maisonfaradja.org", Positions.KPTA);
			employeeService.addNewEmployee("MBUTA", "NKANGI", "Frederic","MAFA015UU", "fredericmbuta@maisonfaradja.org", Positions.SIM);
			employeeService.addNewEmployee("MUNONGO", "FUTILA", "Nova","MAFA004UU", "novamunongo@maisonfaradja.org", Positions.DAF);
//			employeeService.addNewEmployee("MWAMBA", "ILUNGA", "Dyese","0012", "dyese@maisonfaradja.org", Positions.LOG_SUPPLY_CHAIN);
			employeeService.addNewEmployee("ILUNGA", "KABWE", "Vital", "MAFA018UU","vitalilunga@maisonfaradja.org", Positions.IT_OFFICER_COMM_FP);
//			Add roles
			for(RolesNames roleName:RolesNames.values()) {
				appRoleRepository.save(new Role(null,roleName.name()));
			}
			//define a supervisor
			
			employeeService.setSupervisorToEmployee("NGOIE", "NGOY");
			employeeService.setSupervisorToEmployee("NGOY", "MUNONGO");
			employeeService.setSupervisorToEmployee("NGOY", "KALWA");
			employeeService.setSupervisorToEmployee("MUNONGO", "SEYA");
			employeeService.setSupervisorToEmployee("MUNONGO", "NGOIE");
			employeeService.setSupervisorToEmployee("KALWA", "KYUNGU");
			employeeService.setSupervisorToEmployee("KALWA", "MUCHECHE");
//			employeeService.setSupervisorToEmployee("KALWA", "KYUNGU");
//			employeeService.setSupervisorToEmployee("KALWA", "MUCHECHE");
//			employeeService.setSupervisorToEmployee("NGOY", "KALWA");
			employeeService.setSupervisorToEmployee("NGOY", "MBUTA");
//			employeeService.setSupervisorToEmployee("MBUTA", "MUKUMBA");
			employeeService.setSupervisorToEmployee("MBUTA", "ILUNGA");
//			employeeService.setSupervisorToEmployee("MBUTA", "MUKUMBA");
//			employeeService.setSupervisorToEmployee("MWAMBA", "SEYA");
			
//			Add users
			appUserService.saveUser(new AppUser("Vital", "12345", "MAFA018UU"));
//			appUserService.saveUser(new AppUser("Tresor", "12345", "0002"));
//			appUserService.saveUser(new AppUser("Nova", "12345", employeeService.getEmployeeID("NOVA")));
//			appUserService.saveUser(new AppUser("Francine", "12345", employeeService.getEmployeeID("SEYA")));
//			appUserService.saveUser(new AppUser("Tresor", "12345", employeeService.getEmployeeID("KYUNGU")));
//			appUserService.saveUser(new AppUser("Jean-Paul", "12345", employeeService.getEmployeeID("MUCHECHE")));
//			appUserService.saveUser(new AppUser("Dyese", "12345", employeeService.getEmployeeID("MWAMBA")));
//			appUserService.saveUser(new AppUser("Curtis", "12345", employeeService.getEmployeeID("MUKUMBA")));
//			appUserService.saveUser(new AppUser("Vital", "123456", employeeService.getEmployeeID("ILUNGA")));
//			appUserService.saveUser(new AppUser("Clement", "12345", employeeService.getEmployeeID("KASHIKA")));
//			appUserService.saveUser(new AppUser("Michelle", "12345", employeeService.getEmployeeID("BOMASI")));
//			Add roles to users
			appUserService.addRoleToUser("Vital", "USER");	
			appUserService.addRoleToUser("Vital", "SUPERVISOR");	
			appUserService.addRoleToUser("Vital", "HR");
			appUserService.addRoleToUser("Vital", "ADMIN");	
//			appUserService.addRoleToUser("Francine", "DAF");	
//			appUserService.addRoleToUser("Tresor", "USER");	
//			appUserService.addRoleToUser("Vital", "ADMIN");
//			appUserService.addRoleToUser("Vital", "SUPERVISOR");
//			appUserService.addRoleToUser("Vital", "HR");
//			appUserService.addRoleToUser("Nova", "USER");	
//			appUserService.addRoleToUser("Nova", "SUPERVISOR");	
//			appUserService.addRoleToUser("Nova", "DAF");	
//			appUserService.addRoleToUser("Curtis", "USER");	
//			appUserService.addRoleToUser("Francine", "USER");	
//			appUserService.addRoleToUser("Dyese", "USER");	
//			appUserService.addRoleToUser("Dyese", "SUPERVISOR");	
//			appUserService.addRoleToUser("Michelle", "USER");	
//			appUserService.addRoleToUser("Clement", "USER");	
			//Creating a timesheet line
			TimesheetPeriods tp=new TimesheetPeriods();
			List<Sheetday>sheetdays=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA018UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays2=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA013UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays3=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA001UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays4=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA004UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays5=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA002UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays6=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA011UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays7=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA005UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays8=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA015UU", "", "USAID ULINZI");
			List<Sheetday>sheetdays9=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), "MAFA009UU", "", "USAID ULINZI");
//			List<Sheetday>sheetdays10=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KALWA"), "", "USAID ULINZI");
//			List<Sheetday>sheetdays11=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MUCHECHE"), "", "USAID ULINZI");
//			List<Sheetday>sheetdays12=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KYUNGU"), "", "USAID ULINZI");
			sheetdayService.saveTimesheetLine(sheetdays);
			sheetdayService.saveTimesheetLine(sheetdays2);
			sheetdayService.saveTimesheetLine(sheetdays3);
			sheetdayService.saveTimesheetLine(sheetdays4);
			sheetdayService.saveTimesheetLine(sheetdays5);
			sheetdayService.saveTimesheetLine(sheetdays6);
			sheetdayService.saveTimesheetLine(sheetdays7);
			sheetdayService.saveTimesheetLine(sheetdays8);
			sheetdayService.saveTimesheetLine(sheetdays9);
//			sheetdayService.saveTimesheetLine(sheetdays10);
//			sheetdayService.saveTimesheetLine(sheetdays11);
//			sheetdayService.saveTimesheetLine(sheetdays12);
			TimesheetSaver userSavedSl=new TimesheetSaver(null, "MAFA018UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS2=new TimesheetSaver(null, "MAFA001UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS3=new TimesheetSaver(null, "MAFA002UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS4=new TimesheetSaver(null, "MAFA004UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS5=new TimesheetSaver(null, "MAFA005UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS6=new TimesheetSaver(null, "MAFA015UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS7=new TimesheetSaver(null, "MAFA011UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS8=new TimesheetSaver(null, "MAFA013UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS9=new TimesheetSaver(null, "MAFA009UU", tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS10=new TimesheetSaver(null, employeeService.getEmployeeID("KALWA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS11=new TimesheetSaver(null, employeeService.getEmployeeID("MUCHECHE"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			TimesheetSaver userSavedS12=new TimesheetSaver(null, employeeService.getEmployeeID("KYUNGU"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			
			savedSheetLineRepository.save(userSavedSl);
			savedSheetLineRepository.save(userSavedS2);
			savedSheetLineRepository.save(userSavedS3);
			savedSheetLineRepository.save(userSavedS4);
			savedSheetLineRepository.save(userSavedS5);
			savedSheetLineRepository.save(userSavedS6);
			savedSheetLineRepository.save(userSavedS7);
			savedSheetLineRepository.save(userSavedS8);
			savedSheetLineRepository.save(userSavedS9);
//			savedSheetLineRepository.save(userSavedS10);
//			savedSheetLineRepository.save(userSavedS11);
//			savedSheetLineRepository.save(userSavedS12);
//			
			
		}; 
	}	 
}
