import { useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { Loader2 } from 'lucide-react';

import { Button } from '@/components/ui/button';
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetFooter,
  SheetHeader,
  SheetTitle,
} from '@/components/ui/sheet';
import { usePayeeDetails } from '../context/payee-details-provider';
import {
  useCreatePayeeDetails,
  useUpdatePayeeDetails,
} from '../api/payee-details-api';
import {
  payeeDetailsSchema,
  type PayeeDetailsFormData,
} from '../api/schema';
import { useQuery } from '@tanstack/react-query';
import { getAllBanksList } from '@/lib/banks-api';

export function PayeeDetailsDrawer() {
  const { isDrawerOpen, selectedPayeeDetails, closeDrawer } =
    usePayeeDetails();

  const createMutation = useCreatePayeeDetails();
  const updateMutation = useUpdatePayeeDetails();

  const { data: banksResponse } = useQuery({
    queryKey: ['banks', 'list'],
    queryFn: getAllBanksList,
  });

  const banks = banksResponse?.data || [];

  const form = useForm<PayeeDetailsFormData>({
    resolver: zodResolver(payeeDetailsSchema),
    defaultValues: {
      payeeName: '',
      panNumber: '',
      aadhaarNumber: '',
      bankId: undefined,
      ifscCode: '',
      beneficiaryName: '',
      accountNumber: '',
    },
  });

  useEffect(() => {
    if (selectedPayeeDetails) {
      form.reset({
        payeeName: selectedPayeeDetails.payeeName,
        panNumber: selectedPayeeDetails.panNumber || '',
        aadhaarNumber: selectedPayeeDetails.aadhaarNumber || '',
        bankId: selectedPayeeDetails.bankId || undefined,
        ifscCode: selectedPayeeDetails.ifscCode || '',
        beneficiaryName: selectedPayeeDetails.beneficiaryName || '',
        accountNumber: selectedPayeeDetails.accountNumber || '',
      });
    } else {
      form.reset({
        payeeName: '',
        panNumber: '',
        aadhaarNumber: '',
        bankId: undefined,
        ifscCode: '',
        beneficiaryName: '',
        accountNumber: '',
      });
    }
  }, [selectedPayeeDetails, form]);

  const onSubmit = async (data: PayeeDetailsFormData) => {
    // Convert empty strings to undefined for optional fields
    const payload = {
      ...data,
      panNumber: data.panNumber || undefined,
      aadhaarNumber: data.aadhaarNumber || undefined,
      bankId: data.bankId || undefined,
      ifscCode: data.ifscCode || undefined,
      beneficiaryName: data.beneficiaryName || undefined,
      accountNumber: data.accountNumber || undefined,
    };

    if (selectedPayeeDetails) {
      updateMutation.mutate(
        {
          id: selectedPayeeDetails.id,
          data: payload,
        },
        {
          onSuccess: () => {
            closeDrawer();
            form.reset();
          },
        }
      );
    } else {
      createMutation.mutate(payload, {
        onSuccess: () => {
          closeDrawer();
          form.reset();
        },
      });
    }
  };

  const handlePANChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    field: any
  ) => {
    const value = e.target.value.toUpperCase();
    field.onChange(value);
  };

  const handleIFSCChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    field: any
  ) => {
    const value = e.target.value.toUpperCase();
    field.onChange(value);
  };

  return (
    <Sheet open={isDrawerOpen} onOpenChange={closeDrawer}>
      <SheetContent className="flex flex-col">
        <SheetHeader className="text-start">
          <SheetTitle>
            {selectedPayeeDetails ? 'Update' : 'Create'} Payee Details
          </SheetTitle>
          <SheetDescription>
            {selectedPayeeDetails
              ? 'Update the payee banking and identification details.'
              : 'Add new payee banking and identification details.'}
            Click save when you&apos;re done.
          </SheetDescription>
        </SheetHeader>

        <Form {...form}>
          <form
            id="payee-details-form"
            onSubmit={form.handleSubmit(onSubmit)}
            className="flex-1 space-y-6 overflow-y-auto px-4"
          >
            {/* Payee Name */}
            <FormField
              control={form.control}
              name="payeeName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Payee Name *</FormLabel>
                  <FormControl>
                    <Input placeholder="Enter payee name" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* PAN and Aadhaar */}
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="panNumber"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>PAN Number</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="ABCDE1234F"
                        maxLength={10}
                        className="font-mono uppercase"
                        {...field}
                        onChange={(e) => handlePANChange(e, field)}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="aadhaarNumber"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Aadhaar Number</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="123456789012"
                        maxLength={12}
                        className="font-mono"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            {/* Bank */}
            <FormField
              control={form.control}
              name="bankId"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bank</FormLabel>
                  <Select
                    onValueChange={(value) => field.onChange(Number(value))}
                    value={field.value?.toString() || ''}
                  >
                    <FormControl>
                      <SelectTrigger>
                        <SelectValue placeholder="Select a bank" />
                      </SelectTrigger>
                    </FormControl>
                    <SelectContent>
                      {banks.map((bank) => (
                        <SelectItem key={bank.id} value={bank.id.toString()}>
                          {bank.bankName}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                  <FormMessage />
                </FormItem>
              )}
            />

            {/* IFSC Code and Beneficiary Name */}
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <FormField
                control={form.control}
                name="ifscCode"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>IFSC Code</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="SBIN0001234"
                        maxLength={11}
                        className="font-mono uppercase"
                        {...field}
                        onChange={(e) => handleIFSCChange(e, field)}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="beneficiaryName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Beneficiary Name</FormLabel>
                    <FormControl>
                      <Input placeholder="Enter beneficiary name" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>

            {/* Account Number */}
            <FormField
              control={form.control}
              name="accountNumber"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Account Number</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Enter account number (9-18 digits)"
                      maxLength={18}
                      className="font-mono"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </form>
        </Form>

        <SheetFooter className="flex-shrink-0 gap-2 px-4 sm:space-x-0">
          <Button
            variant="outline"
            onClick={closeDrawer}
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            Cancel
          </Button>
          <Button
            type="submit"
            form="payee-details-form"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {(createMutation.isPending || updateMutation.isPending) && (
              <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            )}
            {selectedPayeeDetails ? 'Update' : 'Save'}
          </Button>
        </SheetFooter>
      </SheetContent>
    </Sheet>
  );
}
