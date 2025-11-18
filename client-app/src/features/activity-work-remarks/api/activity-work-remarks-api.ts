import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { toast } from 'sonner'
import api from '@/lib/api'
import { type BackendPageResponse, flattenPageResponse } from '@/lib/api-utils'
import type { ActivityWorkRemark, ActivityWorkRemarkRequest } from './schema'

const REMARK_ENDPOINTS = {
  BASE: '/api/activity-work-remarks',
  BY_ACTIVITY_WORK: (activityWorkId: number) =>
    `/api/activity-work-remarks/activity-work/${activityWorkId}`,
  LIST_BY_ACTIVITY_WORK: (activityWorkId: number) =>
    `/api/activity-work-remarks/activity-work/${activityWorkId}/list`,
  COUNT_BY_ACTIVITY_WORK: (activityWorkId: number) =>
    `/api/activity-work-remarks/activity-work/${activityWorkId}/count`,
  BY_ID: (id: number) => `/api/activity-work-remarks/${id}`,
}

interface ApiResponse<T> {
  data: T
  message: string
  status: number
  timestamp: string
}

export const activityWorkRemarksApi = {
  useGetByActivityWorkId: (
    activityWorkId: number,
    params: {
      page: number
      size: number
      sortBy?: string
      sortOrder?: string
    }
  ) => {
    return useQuery({
      queryKey: ['activity-work-remarks', activityWorkId, params],
      queryFn: async () => {
        const response = await api.get<ApiResponse<BackendPageResponse<ActivityWorkRemark>>>(
          REMARK_ENDPOINTS.BY_ACTIVITY_WORK(activityWorkId),
          {
            params: {
              page: params.page,
              size: params.size,
              ...(params.sortBy && { sortBy: params.sortBy }),
              ...(params.sortOrder && { sortDirection: params.sortOrder }),
            },
          }
        )
        return flattenPageResponse(response.data.data)
      },
      enabled: !!activityWorkId,
    })
  },

  useGetListByActivityWorkId: (activityWorkId: number) => {
    return useQuery({
      queryKey: ['activity-work-remarks-list', activityWorkId],
      queryFn: async () => {
        const response = await api.get<ApiResponse<ActivityWorkRemark[]>>(
          REMARK_ENDPOINTS.LIST_BY_ACTIVITY_WORK(activityWorkId)
        )
        return response.data.data
      },
      enabled: !!activityWorkId,
    })
  },

  useGetCount: (activityWorkId: number) => {
    return useQuery({
      queryKey: ['activity-work-remarks-count', activityWorkId],
      queryFn: async () => {
        const response = await api.get<ApiResponse<number>>(
          REMARK_ENDPOINTS.COUNT_BY_ACTIVITY_WORK(activityWorkId)
        )
        return response.data.data
      },
      enabled: !!activityWorkId,
    })
  },

  useGetById: (id: number | null) => {
    return useQuery({
      queryKey: ['activity-work-remarks', id],
      queryFn: async () => {
        if (!id) throw new Error('ID is required')
        const response = await api.get<ApiResponse<ActivityWorkRemark>>(
          REMARK_ENDPOINTS.BY_ID(id)
        )
        return response.data.data
      },
      enabled: !!id,
    })
  },

  useCreate: () => {
    const queryClient = useQueryClient()

    return useMutation({
      mutationFn: async (data: ActivityWorkRemarkRequest) => {
        const response = await api.post<ApiResponse<ActivityWorkRemark>>(
          REMARK_ENDPOINTS.BASE,
          data
        )
        return response.data.data
      },
      onSuccess: (_, variables) => {
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks', variables.activityWorkId],
        })
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks-list', variables.activityWorkId],
        })
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks-count', variables.activityWorkId],
        })
        toast.success('Remark added successfully')
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || 'Failed to add remark';
        toast.error(message);
      },
    })
  },

  useUpdate: () => {
    const queryClient = useQueryClient()

    return useMutation({
      mutationFn: async ({ id, data }: { id: number; data: ActivityWorkRemarkRequest }) => {
        const response = await api.put<ApiResponse<ActivityWorkRemark>>(
          REMARK_ENDPOINTS.BY_ID(id),
          data
        )
        return response.data.data
      },
      onSuccess: (data) => {
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks', data.activityWorkId],
        })
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks-list', data.activityWorkId],
        })
        toast.success('Remark updated successfully')
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || 'Failed to update remark';
        toast.error(message);
      },
    })
  },

  useDelete: () => {
    const queryClient = useQueryClient()

    return useMutation({
      mutationFn: async ({ id, activityWorkId }: { id: number; activityWorkId: number }) => {
        await api.delete(REMARK_ENDPOINTS.BY_ID(id))
        return activityWorkId
      },
      onSuccess: (activityWorkId) => {
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks', activityWorkId],
        })
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks-list', activityWorkId],
        })
        queryClient.invalidateQueries({
          queryKey: ['activity-work-remarks-count', activityWorkId],
        })
        toast.success('Remark deleted successfully')
      },
      onError: (error: any) => {
        const message = error?.response?.data?.message || error?.message || 'Failed to delete remark';
        toast.error(message);
      },
    })
  },
}
