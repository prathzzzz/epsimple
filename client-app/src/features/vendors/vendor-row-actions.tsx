import { MoreHorizontal, Pencil, Trash2 } from 'lucide-react';
import { useState } from 'react';
import { toast } from 'sonner';

import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { ConfirmDialog } from '@/components/confirm-dialog';

import { useVendorContext } from './vendor-provider';
import { type Vendor, useDeleteVendor } from '@/features/vendors/api/vendors-api';
import { useErrorHandler } from '@/hooks/use-error-handler';

interface VendorRowActionsProps {
  vendor: Vendor;
}

export const VendorRowActions = ({ vendor }: VendorRowActionsProps) => {
  const { openEditDrawer } = useVendorContext();
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const deleteVendor = useDeleteVendor();
  const handleError = useErrorHandler();

  const handleEdit = () => {
    openEditDrawer(vendor);
  };

  const handleDelete = async () => {
    try {
      await deleteVendor.mutateAsync(vendor.id);
      toast.success('Vendor deleted successfully');
      setIsDeleteDialogOpen(false);
    } catch (error) {
      const { message } = handleError.handleError(error);
      toast.error(message);
    }
  };

  return (
    <>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button variant="ghost" className="h-8 w-8 p-0">
            <span className="sr-only">Open menu</span>
            <MoreHorizontal className="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          <DropdownMenuLabel>Actions</DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuItem onClick={handleEdit}>
            <Pencil className="mr-2 h-4 w-4" />
            Edit
          </DropdownMenuItem>
          <DropdownMenuItem
            onClick={() => setIsDeleteDialogOpen(true)}
            className="text-destructive focus:text-destructive"
          >
            <Trash2 className="mr-2 h-4 w-4" />
            Delete
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>

      <ConfirmDialog
        open={isDeleteDialogOpen}
        onOpenChange={setIsDeleteDialogOpen}
        handleConfirm={handleDelete}
        title="Delete Vendor"
        desc={`Are you sure you want to delete "${vendor.vendorName}"? This action cannot be undone.`}
        confirmText="Delete"
        isLoading={deleteVendor.isPending}
        destructive
      />
    </>
  );
};
