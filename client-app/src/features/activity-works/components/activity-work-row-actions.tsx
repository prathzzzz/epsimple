import { MoreHorizontal, Pencil, Trash, MessageSquare } from 'lucide-react';
import type { Row } from '@tanstack/react-table';
import { useState } from 'react';

import { Button } from '@/components/ui/button';
import { PermissionGuard } from '@/components/permission-guard';
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import { Badge } from '@/components/ui/badge';
import type { ActivityWork } from '../api/schema';
import { useActivityWork } from '../context/activity-work-provider';
import { ActivityWorkRemarksDialog, activityWorkRemarksApi } from '@/features/activity-work-remarks';

interface ActivityWorkRowActionsProps {
  row: Row<ActivityWork>;
}

export function ActivityWorkRowActions({ row }: ActivityWorkRowActionsProps) {
  const activityWork = row.original;
  const { setSelectedActivityWork, openDrawer, openDeleteDialog } = useActivityWork();
  const [remarksDialogOpen, setRemarksDialogOpen] = useState(false);

  const { data: remarksCount } = activityWorkRemarksApi.useGetCount(activityWork.id);

  const handleEdit = () => {
    setSelectedActivityWork(activityWork);
    openDrawer();
  };

  const handleDelete = () => {
    setSelectedActivityWork(activityWork);
    openDeleteDialog();
  };

  const handleViewRemarks = () => {
    setRemarksDialogOpen(true);
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
          <DropdownMenuItem onClick={handleViewRemarks}>
            <MessageSquare className="mr-2 h-4 w-4" />
            Remarks
            {remarksCount !== undefined && remarksCount > 0 && (
              <Badge variant="secondary" className="ml-2">
                {remarksCount}
              </Badge>
            )}
          </DropdownMenuItem>
          <PermissionGuard permission="ACTIVITY_WORK:UPDATE">
            <DropdownMenuItem onClick={handleEdit}>
              <Pencil className="mr-2 h-4 w-4" />
              Edit
            </DropdownMenuItem>
          </PermissionGuard>
          <PermissionGuard permission="ACTIVITY_WORK:DELETE">
            <DropdownMenuItem onClick={handleDelete} className="text-destructive">
              <Trash className="mr-2 h-4 w-4" />
              Delete
            </DropdownMenuItem>
          </PermissionGuard>
        </DropdownMenuContent>
      </DropdownMenu>

      <ActivityWorkRemarksDialog
        open={remarksDialogOpen}
        onOpenChange={setRemarksDialogOpen}
        activityWorkId={activityWork.id}
        activityWorkName={activityWork.activitiesName || 'Activity Work'}
      />
    </>
  );
}
