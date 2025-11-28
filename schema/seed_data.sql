-- =====================================================
-- SEED DATA FOR EPSIMPLE DATABASE
-- Generated: 2025-11-25
-- Note: Excludes users, roles, permissions tables (already seeded)
-- =====================================================

-- Set timezone and basic settings
SET statement_timeout = 0;
SET client_encoding = 'UTF8';

-- =====================================================
-- BEGIN TRANSACTION
-- All inserts are wrapped in a transaction for atomicity
-- If any insert fails, all changes will be rolled back
-- =====================================================
BEGIN;

-- =====================================================
-- LEVEL 0: Tables with NO foreign key dependencies
-- =====================================================

-- -----------------------------------------------------
-- STATE (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.state (id, created_at, created_by, updated_at, updated_by, state_code, state_code_alt, state_name) VALUES
(1, NOW(), 1, NOW(), 1, 'MH', 'MAH', 'Maharashtra'),
(2, NOW(), 1, NOW(), 1, 'KA', 'KAR', 'Karnataka'),
(3, NOW(), 1, NOW(), 1, 'TN', 'TAM', 'Tamil Nadu'),
(4, NOW(), 1, NOW(), 1, 'GJ', 'GUJ', 'Gujarat'),
(5, NOW(), 1, NOW(), 1, 'RJ', 'RAJ', 'Rajasthan'),
(6, NOW(), 1, NOW(), 1, 'DL', 'DEL', 'Delhi'),
(7, NOW(), 1, NOW(), 1, 'UP', 'UTP', 'Uttar Pradesh'),
(8, NOW(), 1, NOW(), 1, 'WB', 'WBN', 'West Bengal'),
(9, NOW(), 1, NOW(), 1, 'AP', 'AND', 'Andhra Pradesh'),
(10, NOW(), 1, NOW(), 1, 'TS', 'TEL', 'Telangana');

SELECT setval('state_id_seq', 10);

-- -----------------------------------------------------
-- BANK (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.bank (id, created_at, created_by, updated_at, updated_by, bank_code_alt, bank_logo, bank_name, eps_bank_code, rbi_bank_code) VALUES
(1, NOW(), 1, NOW(), 1, 'SBI', NULL, 'State Bank of India', 'EPS001', 'SBIN0001'),
(2, NOW(), 1, NOW(), 1, 'HDFC', NULL, 'HDFC Bank', 'EPS002', 'HDFC0001'),
(3, NOW(), 1, NOW(), 1, 'ICICI', NULL, 'ICICI Bank', 'EPS003', 'ICIC0001'),
(4, NOW(), 1, NOW(), 1, 'AXIS', NULL, 'Axis Bank', 'EPS004', 'UTIB0001'),
(5, NOW(), 1, NOW(), 1, 'PNB', NULL, 'Punjab National Bank', 'EPS005', 'PUNB0001'),
(6, NOW(), 1, NOW(), 1, 'BOB', NULL, 'Bank of Baroda', 'EPS006', 'BARB0001'),
(7, NOW(), 1, NOW(), 1, 'KOTAK', NULL, 'Kotak Mahindra Bank', 'EPS007', 'KKBK0001'),
(8, NOW(), 1, NOW(), 1, 'YES', NULL, 'Yes Bank', 'EPS008', 'YESB0001'),
(9, NOW(), 1, NOW(), 1, 'IDBI', NULL, 'IDBI Bank', 'EPS009', 'IBKL0001'),
(10, NOW(), 1, NOW(), 1, 'CANARA', NULL, 'Canara Bank', 'EPS010', 'CNRB0001');

SELECT setval('bank_id_seq', 10);

-- -----------------------------------------------------
-- GENERIC_STATUS_TYPE (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.generic_status_type (id, created_at, created_by, updated_at, updated_by, description, status_code, status_name) VALUES
(1, NOW(), 1, NOW(), 1, 'Item is currently active', 'ACTIVE', 'Active'),
(2, NOW(), 1, NOW(), 1, 'Item is currently inactive', 'INACTIVE', 'Inactive'),
(3, NOW(), 1, NOW(), 1, 'Item is pending approval', 'PENDING', 'Pending'),
(4, NOW(), 1, NOW(), 1, 'Item has been approved', 'APPROVED', 'Approved'),
(5, NOW(), 1, NOW(), 1, 'Item has been rejected', 'REJECTED', 'Rejected'),
(6, NOW(), 1, NOW(), 1, 'Work is in progress', 'IN_PROGRESS', 'In Progress'),
(7, NOW(), 1, NOW(), 1, 'Work has been completed', 'COMPLETED', 'Completed'),
(8, NOW(), 1, NOW(), 1, 'Item has been delivered', 'DELIVERED', 'Delivered'),
(9, NOW(), 1, NOW(), 1, 'Asset has been deployed', 'DEPLOYED', 'Deployed'),
(10, NOW(), 1, NOW(), 1, 'Asset has been decommissioned', 'DECOMMISSIONED', 'Decommissioned'),
(11, NOW(), 1, NOW(), 1, 'Asset has been scrapped', 'SCRAPPED', 'Scrapped'),
(12, NOW(), 1, NOW(), 1, 'Asset has been disposed', 'DISPOSED', 'Disposed'),
(13, NOW(), 1, NOW(), 1, 'Item is on hold', 'ON_HOLD', 'On Hold'),
(14, NOW(), 1, NOW(), 1, 'Item has been cancelled', 'CANCELLED', 'Cancelled'),
(15, NOW(), 1, NOW(), 1, 'Site is operational', 'OPERATIONAL', 'Operational');

SELECT setval('generic_status_type_id_seq', 15);

-- -----------------------------------------------------
-- ASSET_CATEGORY (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.asset_category 
(id, created_at, created_by, updated_at, updated_by, asset_code_alt, category_code, category_name, description, depreciation) 
VALUES
(1, NOW(), 1, NOW(), 1, 'ATM', 'ATM001', 'ATM Machine', 'Automated Teller Machines', 10.0),
(2, NOW(), 1, NOW(), 1, 'UPS', 'UPS001', 'UPS', 'Uninterruptible Power Supply Units', 15.0),
(3, NOW(), 1, NOW(), 1, 'AC', 'AC001', 'Air Conditioner', 'Air Conditioning Units', 10.0),
(4, NOW(), 1, NOW(), 1, 'GEN', 'GEN001', 'Generator', 'Power Generators', 12.0),
(5, NOW(), 1, NOW(), 1, 'SRV', 'SRV001', 'Server', 'Server Machines', 20.0),
(6, NOW(), 1, NOW(), 1, 'NET', 'NET001', 'Network Equipment', 'Routers, Switches, Firewalls', 20.0),
(7, NOW(), 1, NOW(), 1, 'CAM', 'CAM001', 'CCTV Camera', 'Security Cameras', 15.0),
(8, NOW(), 1, NOW(), 1, 'SFE', 'SFE001', 'Safe', 'Security Safes', 5.0),
(9, NOW(), 1, NOW(), 1, 'FUR', 'FUR001', 'Furniture', 'Office Furniture', 10.0),
(10, NOW(), 1, NOW(), 1, 'CDM', 'CDM001', 'Cash Deposit Machine', 'Cash Deposit Machines', 10.0);


SELECT setval('asset_category_id_seq', 10);

-- -----------------------------------------------------
-- ASSET_TYPE (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.asset_type (id, created_at, created_by, updated_at, updated_by, description, type_code, type_name) VALUES
(1, NOW(), 1, NOW(), 1, 'Brand new asset', 'NEW', 'New'),
(2, NOW(), 1, NOW(), 1, 'Refurbished asset', 'REFURB', 'Refurbished'),
(3, NOW(), 1, NOW(), 1, 'Leased asset', 'LEASE', 'Leased'),
(4, NOW(), 1, NOW(), 1, 'Rented asset', 'RENT', 'Rented'),
(5, NOW(), 1, NOW(), 1, 'Owned asset', 'OWN', 'Owned'),
(6, NOW(), 1, NOW(), 1, 'Demo unit', 'DEMO', 'Demo'),
(7, NOW(), 1, NOW(), 1, 'Spare asset', 'SPARE', 'Spare'),
(8, NOW(), 1, NOW(), 1, 'Replacement asset', 'REPLACE', 'Replacement');

SELECT setval('asset_type_id_seq', 8);

-- -----------------------------------------------------
-- ASSET_MOVEMENT_TYPE (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.asset_movement_type (id, created_at, created_by, updated_at, updated_by, description, movement_type) VALUES
(1, NOW(), 1, NOW(), 1, 'Movement from factory to warehouse', 'Factory to Warehouse'),
(2, NOW(), 1, NOW(), 1, 'Movement from warehouse to site', 'Warehouse to Site'),
(3, NOW(), 1, NOW(), 1, 'Movement from site to warehouse', 'Site to Warehouse'),
(4, NOW(), 1, NOW(), 1, 'Movement from warehouse to datacenter', 'Warehouse to Datacenter'),
(5, NOW(), 1, NOW(), 1, 'Movement from datacenter to warehouse', 'Datacenter to Warehouse'),
(6, NOW(), 1, NOW(), 1, 'Movement from site to site', 'Site to Site'),
(7, NOW(), 1, NOW(), 1, 'Movement from datacenter to datacenter', 'Datacenter to Datacenter'),
(8, NOW(), 1, NOW(), 1, 'Movement for repair', 'For Repair'),
(9, NOW(), 1, NOW(), 1, 'Return from repair', 'From Repair'),
(10, NOW(), 1, NOW(), 1, 'Movement for disposal', 'For Disposal');

SELECT setval('asset_movement_type_id_seq', 10);

-- -----------------------------------------------------
-- COST_CATEGORY (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.cost_category (id, created_at, created_by, updated_at, updated_by, category_description, category_name) VALUES
(1, NOW(), 1, NOW(), 1, 'Costs related to capital expenditure', 'CAPEX'),
(2, NOW(), 1, NOW(), 1, 'Costs related to operational expenditure', 'OPEX'),
(3, NOW(), 1, NOW(), 1, 'Costs related to maintenance', 'Maintenance'),
(4, NOW(), 1, NOW(), 1, 'Costs related to utilities', 'Utilities'),
(5, NOW(), 1, NOW(), 1, 'Costs related to rent and lease', 'Rent & Lease'),
(6, NOW(), 1, NOW(), 1, 'Costs related to manpower', 'Manpower'),
(7, NOW(), 1, NOW(), 1, 'Costs related to security', 'Security'),
(8, NOW(), 1, NOW(), 1, 'Miscellaneous costs', 'Miscellaneous');

SELECT setval('cost_category_id_seq', 8);

-- -----------------------------------------------------
-- PAYMENT_METHOD (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.payment_method (id, created_at, created_by, updated_at, updated_by, description, method_name) VALUES
(1, NOW(), 1, NOW(), 1, 'National Electronic Funds Transfer', 'NEFT'),
(2, NOW(), 1, NOW(), 1, 'Real Time Gross Settlement', 'RTGS'),
(3, NOW(), 1, NOW(), 1, 'Immediate Payment Service', 'IMPS'),
(4, NOW(), 1, NOW(), 1, 'Unified Payments Interface', 'UPI'),
(5, NOW(), 1, NOW(), 1, 'Cheque payment', 'Cheque'),
(6, NOW(), 1, NOW(), 1, 'Demand Draft', 'DD'),
(7, NOW(), 1, NOW(), 1, 'Cash payment', 'Cash'),
(8, NOW(), 1, NOW(), 1, 'Credit Card payment', 'Credit Card'),
(9, NOW(), 1, NOW(), 1, 'Debit Card payment', 'Debit Card'),
(10, NOW(), 1, NOW(), 1, 'Wire Transfer', 'Wire Transfer');

SELECT setval('payment_method_id_seq', 10);

-- -----------------------------------------------------
-- PAYEE_TYPE (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.payee_type (id, created_at, created_by, updated_at, updated_by, description, payee_category, payee_type) VALUES
(1, NOW(), 1, NOW(), 1, 'Hardware and equipment vendors', 'Vendor', 'Hardware Vendor'),
(2, NOW(), 1, NOW(), 1, 'Software and service vendors', 'Vendor', 'Software Vendor'),
(3, NOW(), 1, NOW(), 1, 'Maintenance service providers', 'Vendor', 'AMC Vendor'),
(4, NOW(), 1, NOW(), 1, 'Property landlords', 'Landlord', 'Property Landlord'),
(5, NOW(), 1, NOW(), 1, 'Equipment lessors', 'Landlord', 'Equipment Lessor'),
(6, NOW(), 1, NOW(), 1, 'Utility service providers', 'Utility', 'Utility Provider'),
(7, NOW(), 1, NOW(), 1, 'Contractors and consultants', 'Contractor', 'Contractor'),
(8, NOW(), 1, NOW(), 1, 'Government and regulatory bodies', 'Government', 'Government Body');

SELECT setval('payee_type_id_seq', 8);

-- -----------------------------------------------------
-- PERSON_TYPE (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.person_type (id, created_at, created_by, updated_at, updated_by, description, type_name) VALUES
(1, NOW(), 1, NOW(), 1, 'Vendor representative or contact', 'Vendor Contact'),
(2, NOW(), 1, NOW(), 1, 'Property landlord individual', 'Landlord'),
(3, NOW(), 1, NOW(), 1, 'Bank official contact', 'Bank Person'),
(4, NOW(), 1, NOW(), 1, 'Regional manager', 'Regional Manager'),
(5, NOW(), 1, NOW(), 1, 'State head', 'State Head'),
(6, NOW(), 1, NOW(), 1, 'Channel manager', 'Channel Manager'),
(7, NOW(), 1, NOW(), 1, 'Master franchisee contact', 'Master Franchisee'),
(8, NOW(), 1, NOW(), 1, 'Site supervisor', 'Site Supervisor'),
(9, NOW(), 1, NOW(), 1, 'Technician', 'Technician'),
(10, NOW(), 1, NOW(), 1, 'Security personnel', 'Security Guard');

SELECT setval('person_type_id_seq', 10);

-- -----------------------------------------------------
-- SITE_CATEGORY (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.site_category (id, created_at, created_by, updated_at, updated_by, category_code, category_name, description) VALUES
(1, NOW(), 1, NOW(), 1, 'ONSITE', 'On-site ATM', 'ATM located within bank premises'),
(2, NOW(), 1, NOW(), 1, 'OFFSITE', 'Off-site ATM', 'ATM located outside bank premises'),
(3, NOW(), 1, NOW(), 1, 'WLA', 'White Label ATM', 'ATM operated by non-bank entities'),
(4, NOW(), 1, NOW(), 1, 'BROWN', 'Brown Label ATM', 'Bank-sponsored ATM managed by third party'),
(5, NOW(), 1, NOW(), 1, 'MOBILE', 'Mobile ATM', 'ATM mounted on vehicle'),
(6, NOW(), 1, NOW(), 1, 'MICRO', 'Micro ATM', 'Handheld micro ATM device');

SELECT setval('site_category_id_seq', 6);

-- -----------------------------------------------------
-- SITE_TYPE (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.site_type (id, created_at, created_by, updated_at, updated_by, description, type_name) VALUES
(1, NOW(), 1, NOW(), 1, 'Through the wall ATM', 'TTW'),
(2, NOW(), 1, NOW(), 1, 'Lobby installation', 'Lobby'),
(3, NOW(), 1, NOW(), 1, 'Kiosk style installation', 'Kiosk'),
(4, NOW(), 1, NOW(), 1, 'Drive through ATM', 'Drive Through'),
(5, NOW(), 1, NOW(), 1, 'Indoor installation', 'Indoor'),
(6, NOW(), 1, NOW(), 1, 'Outdoor installation', 'Outdoor');

SELECT setval('site_type_id_seq', 6);

-- -----------------------------------------------------
-- VENDOR_CATEGORY (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.vendor_category (id, created_at, created_by, updated_at, updated_by, category_name, description) VALUES
(1, NOW(), 1, NOW(), 1, 'OEM', 'Original Equipment Manufacturer'),
(2, NOW(), 1, NOW(), 1, 'Service Provider', 'Service and maintenance providers'),
(3, NOW(), 1, NOW(), 1, 'Distributor', 'Product distributors'),
(4, NOW(), 1, NOW(), 1, 'System Integrator', 'System integration service providers'),
(5, NOW(), 1, NOW(), 1, 'Consumables', 'Consumables and supplies vendors'),
(6, NOW(), 1, NOW(), 1, 'Logistics', 'Logistics and transportation providers'),
(7, NOW(), 1, NOW(), 1, 'Security', 'Security service providers'),
(8, NOW(), 1, NOW(), 1, 'Civil Works', 'Civil and construction contractors');

SELECT setval('vendor_category_id_seq', 8);

-- -----------------------------------------------------
-- ACTIVITY (No FK dependencies)
-- -----------------------------------------------------
INSERT INTO public.activity (id, created_at, created_by, updated_at, updated_by, activity_description, activity_name) VALUES
(1, NOW(), 1, NOW(), 1, 'Installation of ATM machine at site', 'ATM Installation'),
(2, NOW(), 1, NOW(), 1, 'Site survey and feasibility study', 'Site Survey'),
(3, NOW(), 1, NOW(), 1, 'Civil work and construction', 'Civil Work'),
(4, NOW(), 1, NOW(), 1, 'Electrical work and wiring', 'Electrical Work'),
(5, NOW(), 1, NOW(), 1, 'Network and connectivity setup', 'Network Setup'),
(6, NOW(), 1, NOW(), 1, 'Air conditioning installation', 'AC Installation'),
(7, NOW(), 1, NOW(), 1, 'UPS installation and configuration', 'UPS Installation'),
(8, NOW(), 1, NOW(), 1, 'CCTV and security system setup', 'Security Setup'),
(9, NOW(), 1, NOW(), 1, 'Branding and signage work', 'Branding Work'),
(10, NOW(), 1, NOW(), 1, 'Annual maintenance service', 'AMC Service'),
(11, NOW(), 1, NOW(), 1, 'Preventive maintenance', 'PM Service'),
(12, NOW(), 1, NOW(), 1, 'Break-fix repair service', 'Repair Service');

SELECT setval('activity_id_seq', 12);

-- =====================================================
-- LEVEL 1: Tables depending on Level 0 tables
-- =====================================================

-- -----------------------------------------------------
-- CITY (depends on STATE)
-- -----------------------------------------------------
INSERT INTO public.city (id, created_at, created_by, updated_at, updated_by, city_code, city_name, state_id) VALUES
(1, NOW(), 1, NOW(), 1, 'MUM', 'Mumbai', 1),
(2, NOW(), 1, NOW(), 1, 'PUN', 'Pune', 1),
(3, NOW(), 1, NOW(), 1, 'NAG', 'Nagpur', 1),
(4, NOW(), 1, NOW(), 1, 'BLR', 'Bangalore', 2),
(5, NOW(), 1, NOW(), 1, 'MYS', 'Mysore', 2),
(6, NOW(), 1, NOW(), 1, 'CHN', 'Chennai', 3),
(7, NOW(), 1, NOW(), 1, 'COI', 'Coimbatore', 3),
(8, NOW(), 1, NOW(), 1, 'AMD', 'Ahmedabad', 4),
(9, NOW(), 1, NOW(), 1, 'SUR', 'Surat', 4),
(10, NOW(), 1, NOW(), 1, 'JAI', 'Jaipur', 5),
(11, NOW(), 1, NOW(), 1, 'UDR', 'Udaipur', 5),
(12, NOW(), 1, NOW(), 1, 'NDL', 'New Delhi', 6),
(13, NOW(), 1, NOW(), 1, 'LKO', 'Lucknow', 7),
(14, NOW(), 1, NOW(), 1, 'KAN', 'Kanpur', 7),
(15, NOW(), 1, NOW(), 1, 'KOL', 'Kolkata', 8),
(16, NOW(), 1, NOW(), 1, 'HYD', 'Hyderabad', 10),
(17, NOW(), 1, NOW(), 1, 'VIZ', 'Visakhapatnam', 9),
(18, NOW(), 1, NOW(), 1, 'SEC', 'Secunderabad', 10);

SELECT setval('city_id_seq', 18);

-- -----------------------------------------------------
-- COST_TYPE (depends on COST_CATEGORY)
-- -----------------------------------------------------
INSERT INTO public.cost_type (id, created_at, created_by, updated_at, updated_by, type_description, type_name, cost_category_id) VALUES
(1, NOW(), 1, NOW(), 1, 'ATM machine purchase cost', 'ATM Purchase', 1),
(2, NOW(), 1, NOW(), 1, 'UPS equipment purchase', 'UPS Purchase', 1),
(3, NOW(), 1, NOW(), 1, 'AC unit purchase', 'AC Purchase', 1),
(4, NOW(), 1, NOW(), 1, 'Site construction cost', 'Site Construction', 1),
(5, NOW(), 1, NOW(), 1, 'ATM maintenance cost', 'ATM Maintenance', 3),
(6, NOW(), 1, NOW(), 1, 'UPS maintenance cost', 'UPS Maintenance', 3),
(7, NOW(), 1, NOW(), 1, 'AC maintenance cost', 'AC Maintenance', 3),
(8, NOW(), 1, NOW(), 1, 'Electricity charges', 'Electricity', 4),
(9, NOW(), 1, NOW(), 1, 'Water charges', 'Water', 4),
(10, NOW(), 1, NOW(), 1, 'Site rent', 'Site Rent', 5),
(11, NOW(), 1, NOW(), 1, 'Equipment lease', 'Equipment Lease', 5),
(12, NOW(), 1, NOW(), 1, 'Security guard salary', 'Security Salary', 6),
(13, NOW(), 1, NOW(), 1, 'Housekeeping charges', 'Housekeeping', 2),
(14, NOW(), 1, NOW(), 1, 'CRA charges', 'CRA Charges', 2),
(15, NOW(), 1, NOW(), 1, 'Network connectivity charges', 'Network Charges', 2);

SELECT setval('cost_type_id_seq', 15);

-- -----------------------------------------------------
-- VENDOR_TYPE (depends on VENDOR_CATEGORY)
-- -----------------------------------------------------
INSERT INTO public.vendor_type (id, created_at, created_by, updated_at, updated_by, description, type_name, vendor_category_id) VALUES
(1, NOW(), 1, NOW(), 1, 'ATM machine manufacturer', 'ATM OEM', 1),
(2, NOW(), 1, NOW(), 1, 'UPS manufacturer', 'UPS OEM', 1),
(3, NOW(), 1, NOW(), 1, 'AC manufacturer', 'AC OEM', 1),
(4, NOW(), 1, NOW(), 1, 'ATM maintenance service provider', 'ATM AMC', 2),
(5, NOW(), 1, NOW(), 1, 'UPS maintenance service provider', 'UPS AMC', 2),
(6, NOW(), 1, NOW(), 1, 'AC maintenance service provider', 'AC AMC', 2),
(7, NOW(), 1, NOW(), 1, 'Cash replenishment agency', 'CRA', 2),
(8, NOW(), 1, NOW(), 1, 'Network service provider', 'Network Provider', 2),
(9, NOW(), 1, NOW(), 1, 'Civil contractor', 'Civil Contractor', 8),
(10, NOW(), 1, NOW(), 1, 'Electrical contractor', 'Electrical Contractor', 8),
(11, NOW(), 1, NOW(), 1, 'Security agency', 'Security Agency', 7),
(12, NOW(), 1, NOW(), 1, 'Logistics provider', 'Logistics', 6);

SELECT setval('vendor_type_id_seq', 12);

-- -----------------------------------------------------
-- PERSON_DETAILS (depends on PERSON_TYPE)
-- -----------------------------------------------------
INSERT INTO public.person_details (id, created_at, created_by, updated_at, updated_by, contact_number, correspondence_address, first_name, last_name, middle_name, permanent_address, person_type_id) VALUES
-- Bank contacts, managers, etc. (IDs 1-12)
(1, NOW(), 1, NOW(), 1, '9876543210', 'Office 101, Tech Park, Mumbai', 'Rajesh', 'Sharma', NULL, 'Flat 201, Green Valley, Mumbai', 1),
(2, NOW(), 1, NOW(), 1, '9876543211', 'Office 102, IT Hub, Pune', 'Priya', 'Patel', NULL, 'House 45, Koregaon Park, Pune', 1),
(3, NOW(), 1, NOW(), 1, '9876543212', 'Shop 5, Main Road, Bangalore', 'Suresh', 'Kumar', 'K', 'No. 12, MG Road, Bangalore', 2),
(4, NOW(), 1, NOW(), 1, '9876543213', 'Building A, Commercial Complex, Chennai', 'Lakshmi', 'Narayanan', NULL, 'Plot 78, T Nagar, Chennai', 2),
(5, NOW(), 1, NOW(), 1, '9876543214', 'SBI Regional Office, Mumbai', 'Amit', 'Verma', NULL, 'Flat 303, Worli Sea Face, Mumbai', 3),
(6, NOW(), 1, NOW(), 1, '9876543215', 'HDFC Regional Office, Delhi', 'Neha', 'Singh', NULL, 'House 67, Vasant Vihar, Delhi', 3),
(7, NOW(), 1, NOW(), 1, '9876543216', 'Regional Office, Bangalore', 'Vikram', 'Reddy', NULL, 'Villa 23, Whitefield, Bangalore', 4),
(8, NOW(), 1, NOW(), 1, '9876543217', 'State Office, Hyderabad', 'Srinivas', 'Rao', NULL, 'Flat 501, Jubilee Hills, Hyderabad', 5),
(9, NOW(), 1, NOW(), 1, '9876543218', 'Channel Office, Mumbai', 'Kavita', 'Desai', NULL, 'Apt 12, Bandra West, Mumbai', 6),
(10, NOW(), 1, NOW(), 1, '9876543219', 'Franchise Office, Pune', 'Manoj', 'Kulkarni', NULL, 'House 89, Kothrud, Pune', 7),
(11, NOW(), 1, NOW(), 1, '9876543220', 'Site Office, Ahmedabad', 'Jayesh', 'Shah', NULL, 'Flat 45, Satellite, Ahmedabad', 8),
(12, NOW(), 1, NOW(), 1, '9876543221', 'Service Center, Jaipur', 'Ramesh', 'Meena', NULL, 'House 34, C Scheme, Jaipur', 9),
-- Vendor contacts (IDs 13-22) - Each vendor needs a unique person_details record
(13, NOW(), 1, NOW(), 1, '9811111101', 'NCR India Office, Gurgaon', 'Anil', 'Kapoor', NULL, 'DLF Phase 3, Gurgaon', 1),
(14, NOW(), 1, NOW(), 1, '9811111102', 'Diebold Office, Chennai', 'Rajan', 'Krishnan', NULL, 'OMR Road, Chennai', 1),
(15, NOW(), 1, NOW(), 1, '9811111103', 'APC India, Mumbai', 'Sanjay', 'Gupta', NULL, 'Andheri East, Mumbai', 1),
(16, NOW(), 1, NOW(), 1, '9811111104', 'FIS Global, Bangalore', 'Prakash', 'Hegde', NULL, 'Koramangala, Bangalore', 1),
(17, NOW(), 1, NOW(), 1, '9811111105', 'CMS Info Systems, Mumbai', 'Deepak', 'Joshi', NULL, 'Lower Parel, Mumbai', 1),
(18, NOW(), 1, NOW(), 1, '9811111106', 'Airtel Business, Delhi', 'Rahul', 'Khanna', NULL, 'Nehru Place, Delhi', 1),
(19, NOW(), 1, NOW(), 1, '9811111107', 'Civil Works Office, Pune', 'Ganesh', 'Patil', NULL, 'Hadapsar, Pune', 1),
(20, NOW(), 1, NOW(), 1, '9811111108', 'Electrical Services, Mumbai', 'Vinod', 'Nair', NULL, 'Powai, Mumbai', 1),
(21, NOW(), 1, NOW(), 1, '9811111109', 'Security Agency, Delhi', 'Ravi', 'Tiwari', NULL, 'Dwarka, Delhi', 1),
(22, NOW(), 1, NOW(), 1, '9811111110', 'Logistics Hub, Pune', 'Sunil', 'Jadhav', NULL, 'Pimpri, Pune', 1);

SELECT setval('person_details_id_seq', 22);

-- -----------------------------------------------------
-- ACTIVITIES (depends on ACTIVITY)
-- -----------------------------------------------------
INSERT INTO public.activities (id, created_at, created_by, updated_at, updated_by, activity_category, activity_description, activity_name, activity_id) VALUES
(1, NOW(), 1, NOW(), 1, 'Installation', 'Full ATM installation including setup', 'Complete ATM Installation', 1),
(2, NOW(), 1, NOW(), 1, 'Survey', 'Detailed site survey with measurements', 'Detailed Site Survey', 2),
(3, NOW(), 1, NOW(), 1, 'Civil', 'Complete civil work including flooring', 'Full Civil Work', 3),
(4, NOW(), 1, NOW(), 1, 'Electrical', 'Complete electrical work with earthing', 'Full Electrical Work', 4),
(5, NOW(), 1, NOW(), 1, 'Network', 'Network setup with router configuration', 'Network Configuration', 5),
(6, NOW(), 1, NOW(), 1, 'HVAC', 'AC installation with ducting', 'AC Installation Complete', 6),
(7, NOW(), 1, NOW(), 1, 'Power', 'UPS installation with battery backup', 'UPS Setup Complete', 7),
(8, NOW(), 1, NOW(), 1, 'Security', 'CCTV installation with DVR', 'CCTV Setup Complete', 8),
(9, NOW(), 1, NOW(), 1, 'Branding', 'Complete branding with signage', 'Full Branding Work', 9),
(10, NOW(), 1, NOW(), 1, 'Maintenance', 'Quarterly AMC service', 'Quarterly AMC', 10);

SELECT setval('activities_id_seq', 10);

-- -----------------------------------------------------
-- MANAGED_PROJECT (depends on BANK)
-- -----------------------------------------------------
INSERT INTO public.managed_project (id, created_at, created_by, updated_at, updated_by, project_code, project_description, project_name, project_type, bank_id) VALUES
(1, NOW(), 1, NOW(), 1, 'SBI-ATM-2024', 'SBI ATM deployment project for FY 2024-25', 'SBI ATM Expansion 2024', 'ATM Deployment', 1),
(2, NOW(), 1, NOW(), 1, 'HDFC-ATM-2024', 'HDFC ATM deployment project for FY 2024-25', 'HDFC ATM Expansion 2024', 'ATM Deployment', 2),
(3, NOW(), 1, NOW(), 1, 'ICICI-ATM-2024', 'ICICI ATM deployment project for FY 2024-25', 'ICICI ATM Expansion 2024', 'ATM Deployment', 3),
(4, NOW(), 1, NOW(), 1, 'AXIS-ATM-2024', 'Axis Bank ATM deployment project', 'Axis ATM Expansion 2024', 'ATM Deployment', 4),
(5, NOW(), 1, NOW(), 1, 'SBI-AMC-2024', 'SBI ATM maintenance project', 'SBI ATM Maintenance 2024', 'AMC', 1),
(6, NOW(), 1, NOW(), 1, 'HDFC-AMC-2024', 'HDFC ATM maintenance project', 'HDFC ATM Maintenance 2024', 'AMC', 2),
(7, NOW(), 1, NOW(), 1, 'PNB-ATM-2024', 'PNB ATM deployment project', 'PNB ATM Expansion 2024', 'ATM Deployment', 5),
(8, NOW(), 1, NOW(), 1, 'BOB-ATM-2024', 'Bank of Baroda ATM project', 'BOB ATM Expansion 2024', 'ATM Deployment', 6);

SELECT setval('managed_project_id_seq', 8);

-- -----------------------------------------------------
-- COST_ITEM (depends on COST_TYPE)
-- -----------------------------------------------------
INSERT INTO public.cost_item (id, created_at, created_by, updated_at, updated_by, cost_item_for, item_description, cost_type_id) VALUES
(1, NOW(), 1, NOW(), 1, 'NCR ATM Machine', 'NCR SelfServ 84 ATM machine', 1),
(2, NOW(), 1, NOW(), 1, 'Diebold ATM Machine', 'Diebold Nixdorf DN Series ATM', 1),
(3, NOW(), 1, NOW(), 1, 'APC UPS 3KVA', 'APC Smart-UPS 3000VA', 2),
(4, NOW(), 1, NOW(), 1, 'Emerson UPS 5KVA', 'Emerson Liebert GXT4 5000VA', 2),
(5, NOW(), 1, NOW(), 1, 'Daikin AC 1.5T', 'Daikin 1.5 Ton Split AC', 3),
(6, NOW(), 1, NOW(), 1, 'Voltas AC 2T', 'Voltas 2 Ton Split AC', 3),
(7, NOW(), 1, NOW(), 1, 'Site Civil Work', 'Complete civil work for ATM site', 4),
(8, NOW(), 1, NOW(), 1, 'ATM AMC Service', 'Annual maintenance contract for ATM', 5),
(9, NOW(), 1, NOW(), 1, 'UPS AMC Service', 'Annual maintenance for UPS', 6),
(10, NOW(), 1, NOW(), 1, 'AC AMC Service', 'Annual maintenance for AC', 7),
(11, NOW(), 1, NOW(), 1, 'Monthly Electricity', 'Monthly electricity bill', 8),
(12, NOW(), 1, NOW(), 1, 'Monthly Rent', 'Monthly site rent', 10),
(13, NOW(), 1, NOW(), 1, 'Security Service', 'Monthly security guard charges', 12),
(14, NOW(), 1, NOW(), 1, 'CRA Service', 'Cash replenishment service', 14),
(15, NOW(), 1, NOW(), 1, 'Network Lease Line', 'Monthly network charges', 15);

SELECT setval('cost_item_id_seq', 15);

-- =====================================================
-- LEVEL 2: Tables depending on Level 1 tables
-- =====================================================

-- -----------------------------------------------------
-- LOCATION (depends on CITY)
-- -----------------------------------------------------
INSERT INTO public.location (id, created_at, created_by, updated_at, updated_by, address, district, latitude, location_name, longitude, pincode, region, zone, city_id) VALUES
(1, NOW(), 1, NOW(), 1, 'Shop 1, Ground Floor, Nariman Point', 'Mumbai City', '18.9256', 'Nariman Point Branch', '72.8242', '400021', 'West', 'South', 1),
(2, NOW(), 1, NOW(), 1, 'Kiosk 5, Bandra Kurla Complex', 'Mumbai Suburban', '19.0596', 'BKC Main', '72.8655', '400051', 'West', 'Central', 1),
(3, NOW(), 1, NOW(), 1, 'Plot 45, Hinjewadi IT Park', 'Pune', '18.5912', 'Hinjewadi Tech Park', '73.7380', '411057', 'West', 'West', 2),
(4, NOW(), 1, NOW(), 1, 'Site 12, MG Road', 'Bangalore Urban', '12.9758', 'MG Road Junction', '77.6096', '560001', 'South', 'Central', 4),
(5, NOW(), 1, NOW(), 1, 'Building A, Electronic City', 'Bangalore Urban', '12.8456', 'Electronic City Phase 1', '77.6603', '560100', 'South', 'South', 4),
(6, NOW(), 1, NOW(), 1, 'Shop 8, T Nagar', 'Chennai', '13.0418', 'T Nagar Main', '80.2341', '600017', 'South', 'Central', 6),
(7, NOW(), 1, NOW(), 1, 'Kiosk 3, CG Road', 'Ahmedabad', '23.0225', 'CG Road Branch', '72.5714', '380006', 'West', 'Central', 8),
(8, NOW(), 1, NOW(), 1, 'Shop 15, MI Road', 'Jaipur', '26.9124', 'MI Road Main', '75.7873', '302001', 'North', 'Central', 10),
(9, NOW(), 1, NOW(), 1, 'Building 5, Connaught Place', 'New Delhi', '28.6315', 'CP Block A', '77.2167', '110001', 'North', 'Central', 12),
(10, NOW(), 1, NOW(), 1, 'Site 7, Hazratganj', 'Lucknow', '26.8467', 'Hazratganj Main', '80.9462', '226001', 'North', 'Central', 13),
(11, NOW(), 1, NOW(), 1, 'Kiosk 2, Park Street', 'Kolkata', '22.5511', 'Park Street Branch', '88.3524', '700016', 'East', 'Central', 15),
(12, NOW(), 1, NOW(), 1, 'Building B, HITEC City', 'Hyderabad', '17.4485', 'HITEC City Main', '78.3800', '500081', 'South', 'West', 16);

SELECT setval('location_id_seq', 12);

-- -----------------------------------------------------
-- LANDLORD (depends on PERSON_DETAILS)
-- -----------------------------------------------------
INSERT INTO public.landlord (id, created_at, created_by, updated_at, updated_by, rent_share_percentage, landlord_details_id) VALUES
(1, NOW(), 1, NOW(), 1, 100.00, 3),
(2, NOW(), 1, NOW(), 1, 100.00, 4);

SELECT setval('landlord_id_seq', 2);

-- -----------------------------------------------------
-- VENDOR (depends on PERSON_DETAILS, VENDOR_TYPE)
-- -----------------------------------------------------
INSERT INTO public.vendor (id, created_at, created_by, updated_at, updated_by, vendor_code_alt, vendor_details_id, vendor_type_id) VALUES
(1, NOW(), 1, NOW(), 1, 'NCR001', 13, 1),
(2, NOW(), 1, NOW(), 1, 'DBN001', 14, 1),
(3, NOW(), 1, NOW(), 1, 'APC001', 15, 2),
(4, NOW(), 1, NOW(), 1, 'FIS001', 16, 4),
(5, NOW(), 1, NOW(), 1, 'CMS001', 17, 7),
(6, NOW(), 1, NOW(), 1, 'AIR001', 18, 8),
(7, NOW(), 1, NOW(), 1, 'CIV001', 19, 9),
(8, NOW(), 1, NOW(), 1, 'ELC001', 20, 10),
(9, NOW(), 1, NOW(), 1, 'SEC001', 21, 11),
(10, NOW(), 1, NOW(), 1, 'LOG001', 22, 12);

SELECT setval('vendor_id_seq', 10);

-- -----------------------------------------------------
-- SITE_CODE_GENERATOR (depends on MANAGED_PROJECT, STATE)
-- -----------------------------------------------------
INSERT INTO public.site_code_generator (id, created_at, created_by, updated_at, updated_by, max_seq_digit, running_seq, project_id, state_id) VALUES
(1, NOW(), 1, NOW(), 1, 4, 100, 1, 1),
(2, NOW(), 1, NOW(), 1, 4, 50, 1, 2),
(3, NOW(), 1, NOW(), 1, 4, 75, 2, 1),
(4, NOW(), 1, NOW(), 1, 4, 30, 2, 4),
(5, NOW(), 1, NOW(), 1, 4, 25, 3, 3),
(6, NOW(), 1, NOW(), 1, 4, 40, 4, 6);

SELECT setval('site_code_generator_id_seq', 6);

-- =====================================================
-- LEVEL 3: Tables depending on Level 2 tables
-- =====================================================

-- -----------------------------------------------------
-- DATACENTER (depends on LOCATION)
-- -----------------------------------------------------
INSERT INTO public.datacenter (id, created_at, created_by, updated_at, updated_by, datacenter_code, datacenter_name, datacenter_type, location_id) VALUES
(1, NOW(), 1, NOW(), 1, 'DC-MUM-01', 'Mumbai Primary Datacenter', 'Primary', 1),
(2, NOW(), 1, NOW(), 1, 'DC-MUM-02', 'Mumbai DR Datacenter', 'DR', 2),
(3, NOW(), 1, NOW(), 1, 'DC-BLR-01', 'Bangalore Datacenter', 'Primary', 4),
(4, NOW(), 1, NOW(), 1, 'DC-CHN-01', 'Chennai Datacenter', 'Secondary', 6),
(5, NOW(), 1, NOW(), 1, 'DC-DEL-01', 'Delhi Datacenter', 'Primary', 9);

SELECT setval('datacenter_id_seq', 5);

-- -----------------------------------------------------
-- WAREHOUSE (depends on LOCATION)
-- -----------------------------------------------------
INSERT INTO public.warehouse (id, created_at, created_by, updated_at, updated_by, warehouse_code, warehouse_name, warehouse_type, location_id) VALUES
(1, NOW(), 1, NOW(), 1, 'WH-MUM-01', 'Mumbai Central Warehouse', 'Central', 1),
(2, NOW(), 1, NOW(), 1, 'WH-PUN-01', 'Pune Regional Warehouse', 'Regional', 3),
(3, NOW(), 1, NOW(), 1, 'WH-BLR-01', 'Bangalore Warehouse', 'Regional', 4),
(4, NOW(), 1, NOW(), 1, 'WH-CHN-01', 'Chennai Warehouse', 'Regional', 6),
(5, NOW(), 1, NOW(), 1, 'WH-DEL-01', 'Delhi Central Warehouse', 'Central', 9),
(6, NOW(), 1, NOW(), 1, 'WH-HYD-01', 'Hyderabad Warehouse', 'Regional', 12);

SELECT setval('warehouse_id_seq', 6);

-- -----------------------------------------------------
-- PAYEE_DETAILS (depends on BANK - optional)
-- -----------------------------------------------------
INSERT INTO public.payee_details (id, created_at, created_by, updated_at, updated_by, aadhaar_number, aadhaar_number_hash, account_number, account_number_hash, beneficiary_name, beneficiary_name_hash, ifsc_code, pan_number, pan_number_hash, payee_name, bank_id) VALUES
(1, NOW(), 1, NOW(), 1, NULL, NULL, 'encrypted_acc_1', 'hash1', 'encrypted_ben_1', 'hash1', 'SBIN0001234', 'encrypted_pan_1', 'hash1', 'NCR Corporation India Pvt Ltd', 1),
(2, NOW(), 1, NOW(), 1, NULL, NULL, 'encrypted_acc_2', 'hash2', 'encrypted_ben_2', 'hash2', 'HDFC0002345', 'encrypted_pan_2', 'hash2', 'Diebold Nixdorf India Pvt Ltd', 2),
(3, NOW(), 1, NOW(), 1, NULL, NULL, 'encrypted_acc_3', 'hash3', 'encrypted_ben_3', 'hash3', 'ICIC0003456', 'encrypted_pan_3', 'hash3', 'APC India Pvt Ltd', 3),
(4, NOW(), 1, NOW(), 1, NULL, NULL, 'encrypted_acc_4', 'hash4', 'encrypted_ben_4', 'hash4', 'UTIB0004567', 'encrypted_pan_4', 'hash4', 'FIS Global India', 4),
(5, NOW(), 1, NOW(), 1, NULL, NULL, 'encrypted_acc_5', 'hash5', 'encrypted_ben_5', 'hash5', 'SBIN0005678', 'encrypted_pan_5', 'hash5', 'CMS Info Systems Ltd', 1),
(6, NOW(), 1, NOW(), 1, NULL, NULL, 'encrypted_acc_6', 'hash6', 'encrypted_ben_6', 'hash6', 'HDFC0006789', 'encrypted_pan_6', 'hash6', 'Airtel Business', 2),
(7, NOW(), 1, NOW(), 1, 'encrypted_aad_7', 'hash7', 'encrypted_acc_7', 'hash7', 'encrypted_ben_7', 'hash7', 'ICIC0007890', 'encrypted_pan_7', 'hash7', 'Suresh Kumar (Landlord)', 3),
(8, NOW(), 1, NOW(), 1, 'encrypted_aad_8', 'hash8', 'encrypted_acc_8', 'hash8', 'encrypted_ben_8', 'hash8', 'SBIN0008901', 'encrypted_pan_8', 'hash8', 'Lakshmi Narayanan (Landlord)', 1);

SELECT setval('payee_details_id_seq', 8);

-- -----------------------------------------------------
-- PAYEE (depends on PAYEE_DETAILS, PAYEE_TYPE, LANDLORD, VENDOR)
-- -----------------------------------------------------
INSERT INTO public.payee (id, created_at, created_by, updated_at, updated_by, landlord_id, payee_details_id, payee_type_id, vendor_id) VALUES
(1, NOW(), 1, NOW(), 1, NULL, 1, 1, 1),
(2, NOW(), 1, NOW(), 1, NULL, 2, 1, 2),
(3, NOW(), 1, NOW(), 1, NULL, 3, 1, 3),
(4, NOW(), 1, NOW(), 1, NULL, 4, 3, 4),
(5, NOW(), 1, NOW(), 1, NULL, 5, 2, 5),
(6, NOW(), 1, NOW(), 1, NULL, 6, 2, 6),
(7, NOW(), 1, NOW(), 1, 1, 7, 4, NULL),
(8, NOW(), 1, NOW(), 1, 2, 8, 4, NULL);

SELECT setval('payee_id_seq', 8);

-- -----------------------------------------------------
-- PAYMENT_DETAILS (depends on PAYMENT_METHOD)
-- -----------------------------------------------------
INSERT INTO public.payment_details (id, created_at, created_by, updated_at, updated_by, beneficiary_account_number, beneficiary_account_number_hash, beneficiary_name, beneficiary_name_hash, payment_amount, payment_date, payment_remarks, transaction_number, vpa, vpa_hash, payment_method_id) VALUES
(1, NOW(), 1, NOW(), 1, 'enc_acc_1', 'pay_hash1', 'enc_ben_1', 'pay_hash1', 1500000.00, '2024-10-15', 'ATM machine payment - Invoice 1', 'TXN2024100001', NULL, NULL, 1),
(2, NOW(), 1, NOW(), 1, 'enc_acc_2', 'pay_hash2', 'enc_ben_2', 'pay_hash2', 2000000.00, '2024-10-20', 'ATM machine payment - Invoice 2', 'TXN2024100002', NULL, NULL, 2),
(3, NOW(), 1, NOW(), 1, 'enc_acc_3', 'pay_hash3', 'enc_ben_3', 'pay_hash3', 85000.00, '2024-11-01', 'UPS purchase payment', 'TXN2024110001', NULL, NULL, 1),
(4, NOW(), 1, NOW(), 1, 'enc_acc_4', 'pay_hash4', 'enc_ben_4', 'pay_hash4', 45000.00, '2024-11-05', 'AC purchase payment', 'TXN2024110002', NULL, NULL, 1),
(5, NOW(), 1, NOW(), 1, 'enc_acc_5', 'pay_hash5', 'enc_ben_5', 'pay_hash5', 25000.00, '2024-11-10', 'Monthly rent payment', 'TXN2024110003', NULL, NULL, 3),
(6, NOW(), 1, NOW(), 1, 'enc_acc_6', 'pay_hash6', 'enc_ben_6', 'pay_hash6', 15000.00, '2024-11-12', 'AMC service payment', 'TXN2024110004', NULL, NULL, 1);

SELECT setval('payment_details_id_seq', 6);

-- -----------------------------------------------------
-- ASSET_TAG_CODE_GENERATOR (depends on ASSET_CATEGORY, BANK, VENDOR)
-- -----------------------------------------------------
INSERT INTO public.asset_tag_code_generator (id, created_at, created_by, updated_at, updated_by, max_seq_digit, running_seq, asset_category_id, bank_id, vendor_id) VALUES
(1, NOW(), 1, NOW(), 1, 5, 1000, 1, 1, 1),
(2, NOW(), 1, NOW(), 1, 5, 500, 1, 2, 1),
(3, NOW(), 1, NOW(), 1, 5, 750, 1, 1, 2),
(4, NOW(), 1, NOW(), 1, 5, 200, 2, 1, 3),
(5, NOW(), 1, NOW(), 1, 5, 300, 3, 1, 3),
(6, NOW(), 1, NOW(), 1, 5, 150, 1, 3, 1);

SELECT setval('asset_tag_code_generator_id_seq', 6);

-- -----------------------------------------------------
-- SITE (depends on LOCATION, MANAGED_PROJECT, SITE_CATEGORY, SITE_TYPE, GENERIC_STATUS_TYPE, PERSON_DETAILS)
-- -----------------------------------------------------
INSERT INTO public.site (id, created_at, created_by, updated_at, updated_by, ac_units, actual_possession_date, atm_ip, branding_size, cash_live_date, cassette_swap_status, cassette_type_1, cassette_type_2, cassette_type_3, cassette_type_4, connectivity_type, cra_name, ej_docket, fixed_glass_width, gateway_ip, grouting_status, it_stabilizer, location_class, main_door_glass_width, nat_ip, old_site_code, otc_activation_date, otc_activation_status, possession_date, previous_msp_term_id, project_phase, ramp_status, signboard_size, site_close_date, site_code, subnet_mask, switch_ip, tech_live_date, tls_domain_name, tls_port, tss_docket, ups_battery_backup_capacity, bank_person_contact_id, channel_manager_contact_id, location_id, master_franchisee_contact_id, project_id, regional_manager_contact_id, site_category_id, site_status_id, site_type_id, state_head_contact_id) VALUES
(1, NOW(), 1, NOW(), 1, '2', '2024-06-15', '192.168.1.10', 'Large', '2024-07-01', 'Completed', '100', '500', '2000', NULL, 'Leased Line', 'CMS Info Systems', 'EJ001', 1.20, '192.168.1.1', 'Completed', '5KVA', 'Metro', 1.50, '10.0.0.10', NULL, '2024-06-25', 'Activated', '2024-06-01', NULL, 'Phase 1', 'Completed', 'Standard', NULL, 'SBI-MH-0001', '255.255.255.0', '192.168.1.2', '2024-06-20', 'atm1.sbi.co.in', '443', 'TSS001', '30 mins', 5, 9, 1, 10, 1, 7, 2, 15, 1, 8),
(2, NOW(), 1, NOW(), 1, '1', '2024-07-01', '192.168.2.10', 'Medium', '2024-07-20', 'Completed', '100', '500', NULL, NULL, 'VSAT', 'CMS Info Systems', 'EJ002', 1.00, '192.168.2.1', 'Completed', '3KVA', 'Urban', 1.20, '10.0.0.20', NULL, '2024-07-15', 'Activated', '2024-06-20', NULL, 'Phase 1', 'Completed', 'Small', NULL, 'SBI-MH-0002', '255.255.255.0', '192.168.2.2', '2024-07-10', 'atm2.sbi.co.in', '443', 'TSS002', '20 mins', 5, 9, 2, 10, 1, 7, 1, 15, 2, 8),
(3, NOW(), 1, NOW(), 1, '2', '2024-08-01', '192.168.3.10', 'Large', '2024-08-20', 'Completed', '100', '500', '2000', '500', 'Leased Line', 'FIS Global', 'EJ003', 1.50, '192.168.3.1', 'Completed', '5KVA', 'IT Park', 1.80, '10.0.0.30', NULL, '2024-08-15', 'Activated', '2024-07-15', NULL, 'Phase 2', 'Completed', 'Large', NULL, 'HDFC-MH-0001', '255.255.255.0', '192.168.3.2', '2024-08-10', 'atm1.hdfc.com', '443', 'TSS003', '45 mins', 6, 9, 3, 10, 2, 7, 2, 15, 1, 8),
(4, NOW(), 1, NOW(), 1, '2', '2024-08-15', '192.168.4.10', 'Large', '2024-09-01', 'Completed', '100', '500', '2000', NULL, 'Leased Line', 'CMS Info Systems', 'EJ004', 1.20, '192.168.4.1', 'Completed', '5KVA', 'Metro', 1.50, '10.0.0.40', NULL, '2024-08-25', 'Activated', '2024-08-01', NULL, 'Phase 1', 'Completed', 'Standard', NULL, 'ICICI-KA-0001', '255.255.255.0', '192.168.4.2', '2024-08-20', 'atm1.icici.com', '443', 'TSS004', '30 mins', 5, 9, 4, 10, 3, 7, 2, 15, 1, 8),
(5, NOW(), 1, NOW(), 1, '1', '2024-09-01', '192.168.5.10', 'Medium', '2024-09-20', 'Completed', '100', '500', NULL, NULL, 'Broadband', 'FIS Global', 'EJ005', 1.00, '192.168.5.1', 'Completed', '3KVA', 'Suburban', 1.20, '10.0.0.50', NULL, '2024-09-15', 'Activated', '2024-08-20', NULL, 'Phase 1', 'Completed', 'Small', NULL, 'AXIS-DL-0001', '255.255.255.0', '192.168.5.2', '2024-09-10', 'atm1.axis.com', '443', 'TSS005', '20 mins', 6, 9, 9, 10, 4, 7, 1, 15, 3, 8);

SELECT setval('site_id_seq', 5);

-- -----------------------------------------------------
-- ASSET (depends on ASSET_CATEGORY, ASSET_TYPE, BANK, GENERIC_STATUS_TYPE, VENDOR)
-- -----------------------------------------------------
INSERT INTO public.asset (id, created_at, created_by, updated_at, updated_by, asset_name, asset_tag_id, dispatch_order_date, dispatch_order_number, end_of_life_date, end_of_support_date, model_number, purchase_order_cost, purchase_order_date, purchase_order_number, serial_number, warranty_expiry_date, warranty_period, asset_category_id, asset_type_id, lender_bank_id, status_type_id, vendor_id) VALUES
(1, NOW(), 1, NOW(), 1, 'NCR SelfServ 84 ATM', 'ATMSBINCR00001', '2024-05-15', 'DO-2024-001', '2034-05-01', '2029-05-01', 'SS84-001', 1500000.00, '2024-05-01', 'PO-2024-001', 'NCR84-SN-001', '2027-05-01', 36, 1, 1, 1, 9, 1),
(2, NOW(), 1, NOW(), 1, 'NCR SelfServ 84 ATM', 'ATMSBINCR00002', '2024-05-20', 'DO-2024-002', '2034-05-01', '2029-05-01', 'SS84-001', 1500000.00, '2024-05-01', 'PO-2024-001', 'NCR84-SN-002', '2027-05-01', 36, 1, 1, 1, 9, 1),
(3, NOW(), 1, NOW(), 1, 'Diebold DN Series ATM', 'ATMHDFCDBN00001', '2024-06-01', 'DO-2024-003', '2034-06-01', '2029-06-01', 'DN-200', 2000000.00, '2024-05-15', 'PO-2024-002', 'DBN-DN-SN-001', '2027-05-15', 36, 1, 1, 2, 9, 2),
(4, NOW(), 1, NOW(), 1, 'NCR SelfServ 84 ATM', 'ATMICICINCR00001', '2024-07-01', 'DO-2024-004', '2034-07-01', '2029-07-01', 'SS84-001', 1500000.00, '2024-06-15', 'PO-2024-003', 'NCR84-SN-003', '2027-06-15', 36, 1, 1, 3, 9, 1),
(5, NOW(), 1, NOW(), 1, 'NCR SelfServ 84 ATM', 'ATMAXISNCR00001', '2024-08-01', 'DO-2024-005', '2034-08-01', '2029-08-01', 'SS84-001', 1500000.00, '2024-07-15', 'PO-2024-004', 'NCR84-SN-004', '2027-07-15', 36, 1, 1, 4, 9, 1),
(6, NOW(), 1, NOW(), 1, 'APC Smart-UPS 3KVA', 'UPSSBIAPC00001', '2024-05-10', 'DO-2024-006', '2034-05-01', '2029-05-01', 'SUA3000', 85000.00, '2024-04-25', 'PO-2024-005', 'APC-SN-001', '2027-04-25', 36, 2, 1, 1, 9, 3),
(7, NOW(), 1, NOW(), 1, 'APC Smart-UPS 3KVA', 'UPSSBIAPC00002', '2024-05-15', 'DO-2024-007', '2034-05-01', '2029-05-01', 'SUA3000', 85000.00, '2024-04-25', 'PO-2024-005', 'APC-SN-002', '2027-04-25', 36, 2, 1, 1, 9, 3),
(8, NOW(), 1, NOW(), 1, 'Daikin Split AC 1.5T', 'ACSBIDAI00001', '2024-05-08', 'DO-2024-008', '2034-05-01', '2029-05-01', 'FTKF50', 45000.00, '2024-04-20', 'PO-2024-006', 'DAI-SN-001', '2027-04-20', 36, 3, 1, 1, 9, 3),
(9, NOW(), 1, NOW(), 1, 'Daikin Split AC 1.5T', 'ACHDFCDAI00001', '2024-06-05', 'DO-2024-009', '2034-06-01', '2029-06-01', 'FTKF50', 45000.00, '2024-05-20', 'PO-2024-007', 'DAI-SN-002', '2027-05-20', 36, 3, 1, 2, 9, 3),
(10, NOW(), 1, NOW(), 1, 'Hikvision CCTV Camera', 'CAMSBIHIK00001', '2024-05-12', 'DO-2024-010', '2034-05-01', '2029-05-01', 'DS-2CD', 15000.00, '2024-04-28', 'PO-2024-008', 'HIK-SN-001', '2027-04-28', 36, 7, 1, 1, 9, 3);

SELECT setval('asset_id_seq', 10);

-- -----------------------------------------------------
-- ACTIVITY_WORK (depends on ACTIVITIES, GENERIC_STATUS_TYPE, VENDOR)
-- -----------------------------------------------------
INSERT INTO public.activity_work (id, created_at, created_by, updated_at, updated_by, vendor_order_number, work_completion_date, work_order_date, work_start_date, activities_id, status_type_id, vendor_id) VALUES
(1, NOW(), 1, NOW(), 1, 'WO-2024-001', '2024-06-15', '2024-05-01', '2024-05-10', 1, 7, 1),
(2, NOW(), 1, NOW(), 1, 'WO-2024-002', '2024-06-20', '2024-05-15', '2024-05-20', 3, 7, 7),
(3, NOW(), 1, NOW(), 1, 'WO-2024-003', '2024-06-25', '2024-05-20', '2024-05-25', 4, 7, 8),
(4, NOW(), 1, NOW(), 1, 'WO-2024-004', '2024-07-10', '2024-06-01', '2024-06-15', 1, 7, 1),
(5, NOW(), 1, NOW(), 1, 'WO-2024-005', '2024-08-15', '2024-07-01', '2024-07-10', 1, 7, 2),
(6, NOW(), 1, NOW(), 1, 'WO-2024-006', NULL, '2024-10-01', '2024-10-15', 10, 6, 4),
(7, NOW(), 1, NOW(), 1, 'WO-2024-007', NULL, '2024-11-01', NULL, 2, 3, 7),
(8, NOW(), 1, NOW(), 1, 'WO-2024-008', '2024-09-01', '2024-08-01', '2024-08-10', 1, 7, 1);

SELECT setval('activity_work_id_seq', 8);

-- -----------------------------------------------------
-- INVOICE (depends on PAYEE, PAYMENT_DETAILS)
-- -----------------------------------------------------
INSERT INTO public.invoice (id, created_at, created_by, updated_at, updated_by, advance_amount, amount1, amount2, basic_amount, billed_by_vendor_gst, billed_to_eps_gst, cgst, discount_amount, discount_percentage, dispatch_order_date, dispatch_order_number, igst, invoice_date, invoice_number, invoice_received_date, machine_serial_number, master_po_date, master_po_number, net_payable, order_number, paid_date, payment_due_date, payment_status, quantity, remarks, sgst, tax_cgst_percentage, tax_igst_percentage, tax_sgst_percentage, tds, total_amount, total_invoice_value, unit, unit_price, utr_detail, vendor_name, payee_id, payment_details_id) VALUES
(1, NOW(), 1, NOW(), 1, 0.00, NULL, NULL, 1271186.00, '27AAACC1234A1Z5', '27AAACP5678B1Z3', 114406.74, 0.00, 0.00, '2024-05-15', 'DO-2024-001', 0.00, '2024-06-01', 'INV-2024-001', '2024-06-05', 'NCR84-SN-001', '2024-05-01', 'MPO-2024-001', 1500000.00, 'PO-2024-001', '2024-10-15', '2024-07-01', 'Paid', 1.00, 'ATM machine invoice', 114406.74, 9.00, 0.00, 9.00, 0.00, 1500000.00, 1500000.00, 'Unit', 1271186.00, 'UTR2024100001', 'NCR Corporation India', 1, 1),
(2, NOW(), 1, NOW(), 1, 0.00, NULL, NULL, 1694915.00, '27AAACD5678C1Z7', '27AAACP5678B1Z3', 152542.35, 0.00, 0.00, '2024-06-01', 'DO-2024-003', 0.00, '2024-06-15', 'INV-2024-002', '2024-06-20', 'DBN-DN-SN-001', '2024-05-15', 'MPO-2024-002', 2000000.00, 'PO-2024-002', '2024-10-20', '2024-07-15', 'Paid', 1.00, 'Diebold ATM invoice', 152542.35, 9.00, 0.00, 9.00, 0.00, 2000000.00, 2000000.00, 'Unit', 1694915.00, 'UTR2024100002', 'Diebold Nixdorf India', 2, 2),
(3, NOW(), 1, NOW(), 1, 0.00, NULL, NULL, 72033.90, '27AAACA9012D1Z9', '27AAACP5678B1Z3', 6483.05, 0.00, 0.00, '2024-05-10', 'DO-2024-006', 0.00, '2024-05-20', 'INV-2024-003', '2024-05-25', 'APC-SN-001', '2024-04-25', 'MPO-2024-003', 85000.00, 'PO-2024-005', '2024-11-01', '2024-06-20', 'Paid', 1.00, 'UPS invoice', 6483.05, 9.00, 0.00, 9.00, 0.00, 85000.00, 85000.00, 'Unit', 72033.90, 'UTR2024110001', 'APC India Pvt Ltd', 3, 3),
(4, NOW(), 1, NOW(), 1, 0.00, NULL, NULL, 38135.59, '27AAACA9012D1Z9', '27AAACP5678B1Z3', 3432.20, 0.00, 0.00, '2024-05-08', 'DO-2024-008', 0.00, '2024-05-18', 'INV-2024-004', '2024-05-22', 'DAI-SN-001', '2024-04-20', 'MPO-2024-004', 45000.00, 'PO-2024-006', '2024-11-05', '2024-06-18', 'Paid', 1.00, 'AC invoice', 3432.20, 9.00, 0.00, 9.00, 0.00, 45000.00, 45000.00, 'Unit', 38135.59, 'UTR2024110002', 'APC India Pvt Ltd', 3, 4),
(5, NOW(), 1, NOW(), 1, 0.00, NULL, NULL, 25000.00, NULL, '27AAACP5678B1Z3', 0.00, 0.00, 0.00, NULL, NULL, 0.00, '2024-11-01', 'INV-2024-005', '2024-11-03', NULL, NULL, NULL, 25000.00, NULL, '2024-11-10', '2024-11-15', 'Paid', 1.00, 'Monthly rent - November 2024', 0.00, 0.00, 0.00, 0.00, 0.00, 25000.00, 25000.00, 'Month', 25000.00, 'UTR2024110003', 'Suresh Kumar', 7, 5),
(6, NOW(), 1, NOW(), 1, 0.00, NULL, NULL, 12711.86, '27AAACF3456E1Z1', '27AAACP5678B1Z3', 1144.07, 0.00, 0.00, NULL, NULL, 0.00, '2024-11-05', 'INV-2024-006', '2024-11-08', NULL, NULL, NULL, 15000.00, 'AMC-2024-001', '2024-11-12', '2024-12-05', 'Paid', 1.00, 'Quarterly AMC service', 1144.07, 9.00, 0.00, 9.00, 0.00, 15000.00, 15000.00, 'Service', 12711.86, 'UTR2024110004', 'FIS Global India', 4, 6);

SELECT setval('invoice_id_seq', 6);

-- -----------------------------------------------------
-- VOUCHER (depends on PAYEE, PAYMENT_DETAILS)
-- -----------------------------------------------------
INSERT INTO public.voucher (id, created_at, created_by, updated_at, updated_by, amount1, amount2, discount_amount, discount_percentage, final_amount, order_number, payment_due_date, payment_status, quantity, remarks, tax_cgst, tax_igst, tax_sgst, unit, unit_price, voucher_date, voucher_number, payee_id, payment_details_id) VALUES
(1, NOW(), 1, NOW(), 1, NULL, NULL, 0.00, 0.00, 5000.00, NULL, '2024-11-20', 'Pending', 1.00, 'Petty cash - Site visit expenses', 0.00, 0.00, 0.00, 'Service', 5000.00, '2024-11-10', 'VCH-2024-001', 7, NULL),
(2, NOW(), 1, NOW(), 1, NULL, NULL, 0.00, 0.00, 3500.00, NULL, '2024-11-25', 'Pending', 1.00, 'Petty cash - Transportation', 0.00, 0.00, 0.00, 'Service', 3500.00, '2024-11-12', 'VCH-2024-002', 8, NULL),
(3, NOW(), 1, NOW(), 1, NULL, NULL, 0.00, 0.00, 8000.00, NULL, '2024-11-30', 'Pending', 1.00, 'Emergency repair expenses', 0.00, 0.00, 0.00, 'Service', 8000.00, '2024-11-15', 'VCH-2024-003', 4, NULL);

SELECT setval('voucher_id_seq', 3);

-- =====================================================
-- LEVEL 4: Tables depending on Level 3 tables
-- =====================================================

-- -----------------------------------------------------
-- ACTIVITY_WORK_REMARKS (depends on ACTIVITY_WORK)
-- -----------------------------------------------------
INSERT INTO public.activity_work_remarks (id, created_at, created_by, updated_at, updated_by, comment, commented_by, commented_on, activity_work_id) VALUES
(1, NOW(), 1, NOW(), 1, 'ATM installation completed successfully. All tests passed.', 1, '2024-06-15 10:30:00', 1),
(2, NOW(), 1, NOW(), 1, 'Civil work completed. Minor touch-up pending.', 1, '2024-06-18 14:00:00', 2),
(3, NOW(), 1, NOW(), 1, 'Touch-up completed. Site ready for equipment installation.', 1, '2024-06-20 11:00:00', 2),
(4, NOW(), 1, NOW(), 1, 'Electrical work done. Load testing completed.', 1, '2024-06-25 16:30:00', 3),
(5, NOW(), 1, NOW(), 1, 'ATM installation at site 2 completed.', 1, '2024-07-10 12:00:00', 4),
(6, NOW(), 1, NOW(), 1, 'Diebold ATM installed. Software configuration in progress.', 1, '2024-08-12 09:30:00', 5),
(7, NOW(), 1, NOW(), 1, 'Software configuration completed. Cash loading pending.', 1, '2024-08-15 15:00:00', 5),
(8, NOW(), 1, NOW(), 1, 'AMC service scheduled for next week.', 1, '2024-10-20 10:00:00', 6),
(9, NOW(), 1, NOW(), 1, 'Survey team assigned. Site visit planned for Nov 5.', 1, '2024-11-01 09:00:00', 7);

SELECT setval('activity_work_remarks_id_seq', 9);

-- -----------------------------------------------------
-- ASSET_MOVEMENT_TRACKER (depends on ASSET, ASSET_MOVEMENT_TYPE, DATACENTER, SITE, WAREHOUSE)
-- -----------------------------------------------------
INSERT INTO public.asset_movement_tracker (id, created_at, created_by, updated_at, updated_by, from_factory, asset_id, asset_movement_type_id, from_datacenter_id, from_site_id, from_warehouse_id, to_datacenter_id, to_site_id, to_warehouse_id) VALUES
(1, NOW(), 1, NOW(), 1, 'NCR Manesar Factory', 1, 1, NULL, NULL, NULL, NULL, NULL, 1),
(2, NOW(), 1, NOW(), 1, NULL, 1, 2, NULL, NULL, 1, NULL, 1, NULL),
(3, NOW(), 1, NOW(), 1, 'NCR Manesar Factory', 2, 1, NULL, NULL, NULL, NULL, NULL, 1),
(4, NOW(), 1, NOW(), 1, NULL, 2, 2, NULL, NULL, 1, NULL, 2, NULL),
(5, NOW(), 1, NOW(), 1, 'Diebold Chennai Factory', 3, 1, NULL, NULL, NULL, NULL, NULL, 3),
(6, NOW(), 1, NOW(), 1, NULL, 3, 2, NULL, NULL, 3, NULL, 3, NULL),
(7, NOW(), 1, NOW(), 1, 'NCR Manesar Factory', 4, 1, NULL, NULL, NULL, NULL, NULL, 5),
(8, NOW(), 1, NOW(), 1, NULL, 4, 2, NULL, NULL, 5, NULL, 4, NULL),
(9, NOW(), 1, NOW(), 1, 'NCR Manesar Factory', 5, 1, NULL, NULL, NULL, NULL, NULL, 5),
(10, NOW(), 1, NOW(), 1, NULL, 5, 2, NULL, NULL, 5, NULL, 5, NULL),
(11, NOW(), 1, NOW(), 1, 'APC Mumbai Warehouse', 6, 1, NULL, NULL, NULL, NULL, NULL, 1),
(12, NOW(), 1, NOW(), 1, NULL, 6, 2, NULL, NULL, 1, NULL, 1, NULL);

SELECT setval('asset_movement_tracker_id_seq', 12);

-- -----------------------------------------------------
-- EXPENDITURES_INVOICE (depends on COST_ITEM, INVOICE, MANAGED_PROJECT)
-- -----------------------------------------------------
INSERT INTO public.expenditures_invoice (id, created_at, created_by, updated_at, updated_by, description, incurred_date, cost_item_id, invoice_id, managed_project_id) VALUES
(1, NOW(), 1, NOW(), 1, 'ATM machine purchase for SBI project', '2024-06-01', 1, 1, 1),
(2, NOW(), 1, NOW(), 1, 'ATM machine purchase for HDFC project', '2024-06-15', 2, 2, 2),
(3, NOW(), 1, NOW(), 1, 'UPS purchase for SBI sites', '2024-05-20', 3, 3, 1),
(4, NOW(), 1, NOW(), 1, 'AC purchase for SBI sites', '2024-05-18', 5, 4, 1),
(5, NOW(), 1, NOW(), 1, 'Monthly rent for Nariman Point site', '2024-11-01', 12, 5, 1),
(6, NOW(), 1, NOW(), 1, 'AMC service for HDFC ATM', '2024-11-05', 8, 6, 2);

SELECT setval('expenditures_invoice_id_seq', 6);

-- -----------------------------------------------------
-- EXPENDITURES_VOUCHER (depends on COST_ITEM, MANAGED_PROJECT, VOUCHER)
-- -----------------------------------------------------
INSERT INTO public.expenditures_voucher (id, created_at, created_by, updated_at, updated_by, description, incurred_date, cost_item_id, managed_project_id, voucher_id) VALUES
(1, NOW(), 1, NOW(), 1, 'Site visit transportation expenses', '2024-11-10', 15, 1, 1),
(2, NOW(), 1, NOW(), 1, 'Site inspection travel expenses', '2024-11-12', 15, 2, 2),
(3, NOW(), 1, NOW(), 1, 'Emergency UPS repair expenses', '2024-11-15', 9, 1, 3);

SELECT setval('expenditures_voucher_id_seq', 3);

-- =====================================================
-- LEVEL 5: Tables depending on Level 4 tables
-- =====================================================

-- -----------------------------------------------------
-- ASSETS_ON_SITE (depends on ACTIVITY_WORK, ASSET, ASSET_MOVEMENT_TRACKER, GENERIC_STATUS_TYPE, SITE)
-- -----------------------------------------------------
INSERT INTO public.assets_on_site (id, created_at, created_by, updated_at, updated_by, activated_on, assigned_on, decommissioned_on, delivered_on, deployed_on, vacated_on, activity_work_id, asset_id, asset_movement_tracker_id, asset_status_id, site_id) VALUES
(1, NOW(), 1, NOW(), 1, '2024-07-01', '2024-06-10', NULL, '2024-06-08', '2024-06-15', NULL, 1, 1, 2, 9, 1),
(2, NOW(), 1, NOW(), 1, '2024-07-20', '2024-06-25', NULL, '2024-06-22', '2024-07-10', NULL, 4, 2, 4, 9, 2),
(3, NOW(), 1, NOW(), 1, '2024-08-20', '2024-08-01', NULL, '2024-07-28', '2024-08-15', NULL, 5, 3, 6, 9, 3),
(4, NOW(), 1, NOW(), 1, '2024-09-01', '2024-08-15', NULL, '2024-08-12', '2024-08-25', NULL, 8, 4, 8, 9, 4),
(5, NOW(), 1, NOW(), 1, '2024-09-20', '2024-09-05', NULL, '2024-09-02', '2024-09-15', NULL, 8, 5, 10, 9, 5),
(6, NOW(), 1, NOW(), 1, '2024-07-01', '2024-06-12', NULL, '2024-06-10', '2024-06-18', NULL, 1, 6, 12, 9, 1),
(7, NOW(), 1, NOW(), 1, '2024-07-01', '2024-06-12', NULL, '2024-06-10', '2024-06-18', NULL, 1, 8, NULL, 9, 1);

SELECT setval('assets_on_site_id_seq', 7);

-- -----------------------------------------------------
-- ASSETS_ON_DATACENTER (depends on ACTIVITY_WORK, ASSET, ASSET_MOVEMENT_TRACKER, GENERIC_STATUS_TYPE, DATACENTER)
-- -----------------------------------------------------
INSERT INTO public.assets_on_datacenter (id, created_at, created_by, updated_at, updated_by, assigned_on, commissioned_on, delivered_on, disposed_on, vacated_on, activity_work_id, asset_id, asset_movement_tracker_id, asset_status_id, datacenter_id) VALUES
(1, NOW(), 1, NOW(), 1, '2024-06-01', '2024-06-05', '2024-05-28', NULL, NULL, NULL, 10, NULL, 9, 1);

SELECT setval('assets_on_datacenter_id_seq', 1);

-- -----------------------------------------------------
-- ASSETS_ON_WAREHOUSE (depends on ACTIVITY_WORK, ASSET, ASSET_MOVEMENT_TRACKER, GENERIC_STATUS_TYPE, WAREHOUSE)
-- -----------------------------------------------------
INSERT INTO public.assets_on_warehouse (id, created_at, created_by, updated_at, updated_by, assigned_on, commissioned_on, delivered_on, disposed_on, vacated_on, activity_work_id, asset_id, asset_movement_tracker_id, asset_status_id, warehouse_id) VALUES
(1, NOW(), 1, NOW(), 1, '2024-05-16', '2024-05-18', '2024-05-15', NULL, '2024-06-08', NULL, 1, 1, 8, 1),
(2, NOW(), 1, NOW(), 1, '2024-05-21', '2024-05-23', '2024-05-20', NULL, '2024-06-22', NULL, 2, 3, 8, 1),
(3, NOW(), 1, NOW(), 1, '2024-06-02', '2024-06-04', '2024-06-01', NULL, '2024-07-28', NULL, 3, 5, 8, 3),
(4, NOW(), 1, NOW(), 1, '2024-05-11', '2024-05-13', '2024-05-10', NULL, '2024-06-10', NULL, 6, 11, 8, 1),
(5, NOW(), 1, NOW(), 1, '2024-05-16', '2024-05-18', '2024-05-15', NULL, '2024-06-12', NULL, 7, 12, 8, 1);

SELECT setval('assets_on_warehouse_id_seq', 5);

-- -----------------------------------------------------
-- ASSET_EXPENDITURE_AND_ACTIVITY_WORK (depends on ACTIVITY_WORK, ASSET, EXPENDITURES_INVOICE)
-- -----------------------------------------------------
INSERT INTO public.asset_expenditure_and_activity_work (id, created_at, created_by, updated_at, updated_by, activity_work_id, asset_id, expenditures_invoice_id) VALUES
(1, NOW(), 1, NOW(), 1, 1, 1, 1),
(2, NOW(), 1, NOW(), 1, 5, 3, 2),
(3, NOW(), 1, NOW(), 1, 1, 6, 3),
(4, NOW(), 1, NOW(), 1, 1, 8, 4);

SELECT setval('asset_expenditure_and_activity_work_id_seq', 4);

-- -----------------------------------------------------
-- SITE_ACTIVITY_WORK_EXPENDITURE (depends on ACTIVITY_WORK, EXPENDITURES_INVOICE, SITE)
-- -----------------------------------------------------
INSERT INTO public.site_activity_work_expenditure (id, created_at, created_by, updated_at, updated_by, activity_work_id, expenditures_invoice_id, site_id) VALUES
(1, NOW(), 1, NOW(), 1, 1, 1, 1),
(2, NOW(), 1, NOW(), 1, 2, 4, 1),
(3, NOW(), 1, NOW(), 1, 5, 2, 3),
(4, NOW(), 1, NOW(), 1, 6, 6, 3);

SELECT setval('site_activity_work_expenditure_id_seq', 4);

-- =====================================================
-- RESET ALL SEQUENCES TO CORRECT VALUES
-- =====================================================
SELECT setval('state_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.state));
SELECT setval('bank_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.bank));
SELECT setval('generic_status_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.generic_status_type));
SELECT setval('asset_category_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.asset_category));
SELECT setval('asset_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.asset_type));
SELECT setval('asset_movement_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.asset_movement_type));
SELECT setval('cost_category_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.cost_category));
SELECT setval('payment_method_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.payment_method));
SELECT setval('payee_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.payee_type));
SELECT setval('person_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.person_type));
SELECT setval('site_category_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.site_category));
SELECT setval('site_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.site_type));
SELECT setval('vendor_category_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.vendor_category));
SELECT setval('activity_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.activity));
SELECT setval('city_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.city));
SELECT setval('cost_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.cost_type));
SELECT setval('vendor_type_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.vendor_type));
SELECT setval('person_details_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.person_details));
SELECT setval('activities_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.activities));
SELECT setval('managed_project_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.managed_project));
SELECT setval('cost_item_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.cost_item));
SELECT setval('location_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.location));
SELECT setval('landlord_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.landlord));
SELECT setval('vendor_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.vendor));
SELECT setval('site_code_generator_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.site_code_generator));
SELECT setval('datacenter_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.datacenter));
SELECT setval('warehouse_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.warehouse));
SELECT setval('payee_details_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.payee_details));
SELECT setval('payee_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.payee));
SELECT setval('payment_details_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.payment_details));
SELECT setval('asset_tag_code_generator_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.asset_tag_code_generator));
SELECT setval('site_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.site));
SELECT setval('asset_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.asset));
SELECT setval('activity_work_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.activity_work));
SELECT setval('invoice_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.invoice));
SELECT setval('voucher_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.voucher));
SELECT setval('activity_work_remarks_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.activity_work_remarks));
SELECT setval('asset_movement_tracker_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.asset_movement_tracker));
SELECT setval('expenditures_invoice_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.expenditures_invoice));
SELECT setval('expenditures_voucher_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.expenditures_voucher));
SELECT setval('assets_on_site_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.assets_on_site));
SELECT setval('assets_on_datacenter_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.assets_on_datacenter));
SELECT setval('assets_on_warehouse_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.assets_on_warehouse));
SELECT setval('asset_expenditure_and_activity_work_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.asset_expenditure_and_activity_work));
SELECT setval('site_activity_work_expenditure_id_seq', (SELECT COALESCE(MAX(id), 0) FROM public.site_activity_work_expenditure));

-- =====================================================
-- COMMIT TRANSACTION
-- If we reach here, all inserts were successful
-- =====================================================
COMMIT;

-- =====================================================
-- END OF SEED DATA
-- =====================================================

