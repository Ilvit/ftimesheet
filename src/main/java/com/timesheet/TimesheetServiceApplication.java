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
//			employeeService.addNewEmployee("SEYA", "SEYA", "Francine", "fseya@maisonfaradja.org", Positions.RECEPTIONIST);
//			employeeService.setSupervisorToEmployee("KALWA", "KYUNGU");
//			TimesheetPeriods tp=new TimesheetPeriods();
//			List<Sheetday>sheetdays=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGOIE"), "", "USAID ULINZI");
//			sheetdayService.saveTimesheetLine(sheetdays);
//			TimesheetSaver userSavedSl=new TimesheetSaver(null, employeeService.getEmployeeID("NGOIE"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
//			savedSheetLineRepository.save(userSavedSl);
			
			//Projects
			projectRepository.save(new USAIDProject(null,"USAID ULINZI","Key Population project"));
			projectRepository.save(new USAIDProject(null,"EPIC","Epidemiologic Control"));
			
			//Add employees
			employeeService.addNewEmployee("SEYA", "SENEFU", "Francine","MAFA005UU", "fseya@maisonfaradja.org", Positions.RECEPTIONIST);
			employeeService.addNewEmployee("KYUNGU", "MWANANGOY", "Tresor","MAFA013UU", "tresorkyungu@maisonfaradja.org", Positions.SITE_SUPERVISOR_KASAJI);
			employeeService.addNewEmployee("MUCHECHE", "NYANDWE", "Jean-Paul","MAFA011UU", "jpmucheche@maisonfaradja.org", Positions.SITE_SUPERVISOR_KAPANGA);
			employeeService.addNewEmployee("NGOIE", "MUTOMBO", "Didier", "MAFA001UU","didierngoie@maisonfaradja.org", Positions.COP);
			employeeService.addNewEmployee("NGOY", "KABWE", "Delly", "MAFA002UU","dellyngoy@maisonfaradja.org", Positions.DCOP);
			employeeService.addNewEmployee("KASHIKA", "MUTOMBO", "Clement", "MAFA006UU","clementkashika@maisonfaradja.org", Positions.FINANCE_ASSOCIATE);
			employeeService.addNewEmployee("BOMASI", "GIZELA", "Michelle","MAFA012UU", "michelbomasi@maisonfaradja.org", Positions.FINANCE_OFFICER);
			employeeService.addNewEmployee("MUKUMBA", "KAMWANYA", "Curtis","MAFA003UU", "curtismukumba@maisonfaradja.org", Positions.SSI);
			employeeService.addNewEmployee("KALWA", "TUNDWA", "Patty","MAFA009UU", "pattykalwa@maisonfaradja.org", Positions.KPTA);
			employeeService.addNewEmployee("MBUTA", "NKANGI", "Frederic","MAFA015UU", "fredericmbuta@maisonfaradja.org", Positions.SIM);
			employeeService.addNewEmployee("MUNONGO", "FUTILA", "Nova","MAFA004UU", "novamunongo@maisonfaradja.org", Positions.DAF);
			employeeService.addNewEmployee("MWAMBA", "ILUNGA", "Dyese","MAFA007UU", "dyese@maisonfaradja.org", Positions.LOG_SUPPLY_CHAIN);
			employeeService.addNewEmployee("ILUNGA", "KABWE", "Vital","MAFA018UU", "vitalilunga@maisonfaradja.org", Positions.IT_OFFICER_COMM_FP);
			employeeService.addNewEmployee("SAMBA", "YAMBO", "Béatrice","MAFA016UU", "vitalilunga@maisonfaradja.org", Positions.CLEANER_KASAJI);
			employeeService.addNewEmployee("NGOIE3", "SAMBA", "Hervé", "MAFA017UU","vitalilunga@maisonfaradja.org", Positions.DRIVER_KOLWEZI);
			employeeService.addNewEmployee("LUFULWABO", "TSHALA", "Fifi","MAFA014UU", "vitalilunga@maisonfaradja.org", Positions.CLEANER_KOLWEZI);
			employeeService.addNewEmployee("KITWA", "MUYAMPE", "Jérémie","MAFA008UU", "vitalilunga@maisonfaradja.org", Positions.GARDENER_KOLWEZI);
			employeeService.addNewEmployee("NGOIE2", "LUBOMBO", "Michée","MAFA010UU", "micheengoie@maisonfaradja.org", Positions.LOG_ASSOCIATE);
			employeeService.addNewEmployee("NGOYA", "WA MONGA", "Jean", "ngoiewamonga@maisonfaradja.org", Positions.DRIVER_KASAJI);
			employeeService.addNewEmployee("MAISON", "FARADJA", "Ong", "info@maisonfaradja.org", Positions.OTHER);
//			Add roles
			for(RolesNames roleName:RolesNames.values()) {
				appRoleRepository.save(new Role(null,roleName.name()));
			}
			//define a supervisor
			
			employeeService.setSupervisorToEmployee("NGOIE", "ILUNGA");
			
			
//			Add users
			appUserService.saveUser(new AppUser("Didier", "12345", employeeService.getEmployeeID("NGOIE")));
			appUserService.saveUser(new AppUser("Vital", "12345", employeeService.getEmployeeID("NGOY")));
			
//			Add roles to users
			appUserService.addRoleToUser("Didier", "USER");	
			appUserService.addRoleToUser("Didier", "SUPERVISOR");	
			appUserService.addRoleToUser("Didier", "COP");
			appUserService.addRoleToUser("Vital", "USER");	
			appUserService.addRoleToUser("Vital", "ADMIN");
			appUserService.addRoleToUser("Vital", "SUPERVISOR");
			appUserService.addRoleToUser("Vital", "HR");
			//Creating a timesheet line
			TimesheetPeriods tp=new TimesheetPeriods();
			List<Sheetday>sheetdays=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("SEYA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays2=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KYUNGU"), "", "USAID ULINZI");
			List<Sheetday>sheetdays3=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MUCHECHE"), "", "USAID ULINZI");
			List<Sheetday>sheetdays4=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGOIE"), "", "USAID ULINZI");
			List<Sheetday>sheetdays5=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGOY"), "", "USAID ULINZI");
			List<Sheetday>sheetdays6=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KASHIKA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays7=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("BOMASI"), "", "USAID ULINZI");
			List<Sheetday>sheetdays8=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MUKUMBA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays9=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KALWA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays10=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MBUTA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays11=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MUNONGO"), "", "USAID ULINZI");
			List<Sheetday>sheetdays12=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("MWAMBA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays13=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("ILUNGA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays14=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("SAMBA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays15=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGOIE2"), "", "USAID ULINZI");
			List<Sheetday>sheetdays16=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("LUFULWABO"), "", "USAID ULINZI");
			List<Sheetday>sheetdays17=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("KITWA"), "", "USAID ULINZI");
			List<Sheetday>sheetdays18=sheetdayService.getNewTimesheetLine(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeService.getEmployeeID("NGOIE3"), "", "USAID ULINZI");
			
			sheetdayService.saveTimesheetLine(sheetdays);
			sheetdayService.saveTimesheetLine(sheetdays2);
			sheetdayService.saveTimesheetLine(sheetdays3);
			sheetdayService.saveTimesheetLine(sheetdays4);
			sheetdayService.saveTimesheetLine(sheetdays5);
			sheetdayService.saveTimesheetLine(sheetdays6);
			sheetdayService.saveTimesheetLine(sheetdays7);
			sheetdayService.saveTimesheetLine(sheetdays8);
			sheetdayService.saveTimesheetLine(sheetdays9);
			sheetdayService.saveTimesheetLine(sheetdays10);
			sheetdayService.saveTimesheetLine(sheetdays11);
			sheetdayService.saveTimesheetLine(sheetdays12);
			sheetdayService.saveTimesheetLine(sheetdays13);
			sheetdayService.saveTimesheetLine(sheetdays14);
			sheetdayService.saveTimesheetLine(sheetdays15);
			sheetdayService.saveTimesheetLine(sheetdays16);
			sheetdayService.saveTimesheetLine(sheetdays17);
			sheetdayService.saveTimesheetLine(sheetdays18);
			TimesheetSaver userSavedSl=new TimesheetSaver(null, employeeService.getEmployeeID("SEYA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS2=new TimesheetSaver(null, employeeService.getEmployeeID("KYUNGU"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS3=new TimesheetSaver(null, employeeService.getEmployeeID("MUCHECHE"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS4=new TimesheetSaver(null, employeeService.getEmployeeID("NGOIE"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS5=new TimesheetSaver(null, employeeService.getEmployeeID("NGOY"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS6=new TimesheetSaver(null, employeeService.getEmployeeID("KASHIKA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS7=new TimesheetSaver(null, employeeService.getEmployeeID("BOMASI"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS8=new TimesheetSaver(null, employeeService.getEmployeeID("MUKUMBA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS9=new TimesheetSaver(null, employeeService.getEmployeeID("KALWA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS10=new TimesheetSaver(null, employeeService.getEmployeeID("MBUTA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS11=new TimesheetSaver(null, employeeService.getEmployeeID("MUNONGO"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS12=new TimesheetSaver(null, employeeService.getEmployeeID("MWAMBA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS13=new TimesheetSaver(null, employeeService.getEmployeeID("ILUNGA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS14=new TimesheetSaver(null, employeeService.getEmployeeID("SAMBA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS15=new TimesheetSaver(null, employeeService.getEmployeeID("NGOIE2"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS16=new TimesheetSaver(null, employeeService.getEmployeeID("LUFULWABO"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS17=new TimesheetSaver(null, employeeService.getEmployeeID("KITWA"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			TimesheetSaver userSavedS18=new TimesheetSaver(null, employeeService.getEmployeeID("NGOIE3"), tp.getPrecedentPeriod(tp.getCurrentPeriod()), false, false, false, false, false, false);
			
			savedSheetLineRepository.save(userSavedSl);
			savedSheetLineRepository.save(userSavedS2);
			savedSheetLineRepository.save(userSavedS3);
			savedSheetLineRepository.save(userSavedS4);
			savedSheetLineRepository.save(userSavedS5);
			savedSheetLineRepository.save(userSavedS6);
			savedSheetLineRepository.save(userSavedS7);
			savedSheetLineRepository.save(userSavedS8);
			savedSheetLineRepository.save(userSavedS9);
			savedSheetLineRepository.save(userSavedS10);
			savedSheetLineRepository.save(userSavedS11);
			savedSheetLineRepository.save(userSavedS12);
			savedSheetLineRepository.save(userSavedS13);
			savedSheetLineRepository.save(userSavedS14);
			savedSheetLineRepository.save(userSavedS15);
			savedSheetLineRepository.save(userSavedS16);
			savedSheetLineRepository.save(userSavedS17);
			savedSheetLineRepository.save(userSavedS18);
		}; 
	}	 
}
